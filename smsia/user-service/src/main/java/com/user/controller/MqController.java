package com.user.controller;

import com.user.annotations.MsLog;
import com.user.model.Result;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
public class MqController {
    private static final String MQ_SERVER_ADDR = "localhost:9876";
    private static final String PRODUCER_GROUP = "producer-group";
    private static final String PRODUCER_TOPIC = "producer-topic";
    private static final String CONSUMER_GROUP = "consumer-group";
    private static final String CONSUMER_TOPIC = "consumer-topic";

    @MsLog("同步发送消息")
    @RequestMapping(value = "/produce/sync", method = RequestMethod.POST)
    public Result produceSync(@RequestBody String str) {
        Result result = new Result();
        try {
            produceSync();
        } catch (Exception e) {
            result.setCode(-1).setMsg("同步发送消息失败");
            e.printStackTrace();
        }
        return result.setCode(200).setMsg("同步发送消息成功");
    }

    private static void produceSync() throws Exception {
        // Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
        // Specify name server addresses.
        producer.setNamesrvAddr(MQ_SERVER_ADDR);
        // Launch the instance.
        producer.start();
        for (int i = 0; i < 100; i++) {
            // Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(
                    PRODUCER_TOPIC /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            // Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
        // Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }

    @MsLog("异步发送消息")
    @RequestMapping(value = "/produce/Async", method = RequestMethod.POST)
    public Result produceAsync(@RequestBody String str) {
        Result result = new Result();
        try {
            produceAsync();
        } catch (Exception e) {
            result.setCode(-1).setMsg("异步发送消息失败");
            e.printStackTrace();
        }
        return result.setCode(200).setMsg("异步发送消息成功");
    }

    private static void produceAsync() throws Exception {
        // Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
        // Specify name server addresses.
        producer.setNamesrvAddr(MQ_SERVER_ADDR);
        // Launch the instance.
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);

        int messageCount = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(messageCount);
        for (int i = 0; i < messageCount; i++) {
            try {
                final int index = i;
                Message msg = new Message(
                        PRODUCER_TOPIC,
                        "TagA",
                        "OrderID188",
                        "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
                producer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d OK %s \n", index, sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d Exception %s \n", index, e);
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
        producer.shutdown();
    }

    @MsLog("发送消息")
    @RequestMapping(value = "/produce/oneWay", method = RequestMethod.POST)
    public Result produceOneWay(@RequestBody String str) {
        Result result = new Result();
        try {
            produceOneWay();
        } catch (Exception e) {
            result.setCode(-1).setMsg("发送消息失败");
            e.printStackTrace();
        }
        return result.setCode(200).setMsg("发送消息成功");
    }

    private static void produceOneWay() throws Exception {
        // Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
        // Specify name server addresses.
        producer.setNamesrvAddr("localhost:9876");
        // Launch the instance.
        producer.start();
        for (int i = 0; i < 100; i++) {
            // Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(
                    PRODUCER_TOPIC /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            // Call send message to deliver message to one of brokers.
            producer.sendOneway(msg);
        }
        // Wait for sending to complete
        Thread.sleep(5000);
        producer.shutdown();
    }

    @MsLog("消费消息")
    @RequestMapping(value = "/consume", method = RequestMethod.POST)
    public Result consume(@RequestBody String str) {
        Result result = new Result();
        try {
            consume();
        } catch (Exception e) {
            result.setCode(-1).setMsg("消费消息失败");
            e.printStackTrace();
        }
        return result.setCode(200).setMsg("消费消息成功");
    }

    private static void consume() throws Exception {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP);

        // Specify name server addresses.
        consumer.setNamesrvAddr(MQ_SERVER_ADDR);

        // Subscribe one more more topics to consume.
        consumer.subscribe(CONSUMER_TOPIC, "*");
        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s \n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // Launch the consumer instance.
        consumer.start();
        System.out.printf("Consumer Started.\n");
    }
}
