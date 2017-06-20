package com.cyfonly.thriftj.test.thriftserver;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.thrift.TException;

import com.github.xgene.thriftj.service.MonitorNotifyService.Iface;
import com.github.xgene.thriftj.service.VehicleStatus;

public class MonitorNotifyServiceImpl implements Iface {

	private static final AtomicInteger counter=new AtomicInteger(1);
	@Override
	public int notify(VehicleStatus vehicleStatus) throws TException {
		return counter.getAndIncrement();
	}

}
