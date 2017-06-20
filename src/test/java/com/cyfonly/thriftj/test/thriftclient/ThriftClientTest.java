package com.cyfonly.thriftj.test.thriftclient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyfonly.thriftj.ThriftClient;
import com.cyfonly.thriftj.constants.Constant;
import com.cyfonly.thriftj.failover.ConnectionValidator;
import com.cyfonly.thriftj.failover.FailoverStrategy;
import com.github.xgene.thriftj.service.MonitorNotifyService;
import com.github.xgene.thriftj.service.VehicleStatus;


/**
 * TestThriftJ.thrift Client，基于 ThriftJ 组件
 * @author yunfeng.cheng
 * @create 2016-11-21
 */
public class ThriftClientTest {
	private static final Logger logger = LoggerFactory.getLogger(ThriftClientTest.class);
	
	private static final String servers = "127.0.0.1:9080";
	
	private static final AtomicInteger counter=new AtomicInteger(0);
	
	public static void main(String[] args){
		
		ConnectionValidator validator = new ConnectionValidator() {
			@Override
			public boolean isValid(TTransport object) {
				return object.isOpen();
			}
		};
		GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
		FailoverStrategy<?> failoverStrategy = new FailoverStrategy<>();
		
		final ThriftClient thriftClient = new ThriftClient();
		thriftClient.servers(servers)
					.loadBalance(Constant.LoadBalance.RANDOM)
					.connectionValidator(validator)
					.poolConfig(poolConfig)
					.failoverStrategy(failoverStrategy)
					.connTimeout(60)
					.backupServers("")
					.serviceLevel(Constant.ServiceLevel.NOT_EMPTY)
					.start();
		
		ExecutorService  executorService =	Executors.newFixedThreadPool(40);
		
		for (int i = 0; i < 4000; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					MonitorNotifyService.Client client = thriftClient.iface(MonitorNotifyService.Client.class);
					
					VehicleStatus vehicleStatus = new VehicleStatus();
					vehicleStatus.setAddrame("dddd");
					vehicleStatus.setAddrd("addrd");
					vehicleStatus.setCardId("cardId");
					vehicleStatus.setItemShort("itemShort");
					vehicleStatus.setMessageContent("messageContent");
					vehicleStatus.setMessageData("messageData");
					vehicleStatus.setMessageLevel("messageLeve");
					vehicleStatus.setMessageType("messageType");
					vehicleStatus.setModeld("modeld");
					vehicleStatus.setUpdDatetime("updDatetime");
					vehicleStatus.setVehicled("vehicled");
					int result;
					try {
						result = client.notify(vehicleStatus);
						int s= ((result % 2) == 0)? 1 : -1;
						int finl=counter.addAndGet(result * s);
						System.out.println("result=" + finl);
					} catch (TException e) {
						e.printStackTrace();
					}
					
				}
			});
		}
		
//		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				//打印从thriftClient获取到的可用服务列表
//				StringBuffer buffer = new StringBuffer();
//				List<ThriftServer> servers = thriftClient.getAvailableServers();
//				for(ThriftServer server : servers){
//					buffer.append(server.getHost()).append(":").append(server.getPort()).append(",");
//				}
//				logger.info("ThriftServers:[" + (buffer.length() == 0 ? "No avaliable server" : buffer.toString().substring(0, buffer.length()-1)) + "]");
//
//				if(buffer.length() > 0){
//					try {
//						//测试服务是否可用
//						MonitorNotifyService.Client client = thriftClient.iface(MonitorNotifyService.Client.class);
//						
//						VehicleStatus vehicleStatus = new VehicleStatus();
//						vehicleStatus.setAddrame("dddd");
//						vehicleStatus.setAddrd("addrd");
//						vehicleStatus.setCardId("cardId");
//						vehicleStatus.setItemShort("itemShort");
//						vehicleStatus.setMessageContent("messageContent");
//						vehicleStatus.setMessageData("messageData");
//						vehicleStatus.setMessageLevel("messageLeve");
//						vehicleStatus.setMessageType("messageType");
//						vehicleStatus.setModeld("modeld");
//						vehicleStatus.setUpdDatetime("updDatetime");
//						vehicleStatus.setVehicled("vehicled");
//						int result = client.notify(vehicleStatus);
//						System.out.println("result=" + result);
//					} catch(Throwable t){
//						logger.error("-------------exception happen", t);
//					}
//				}
//			}
//		}, 0, 10, TimeUnit.SECONDS);
	}

}
