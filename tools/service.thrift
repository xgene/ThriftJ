namespace java com.github.xgene.thriftj.service

struct VehicleStatus {
	1:string vehicled
	2:string addrd
	3:string addrame
	4:string modeld
	5:string messageContent
	6:string messageLevel
	7:string messageType
	8:string messageData
	9:string updDatetime
	10:string cardId
	11:string itemShort
	12:string deptId
	13:string deptName
}

service  MonitorNotifyService {
  i32 notify(1:VehicleStatus vehicleStatus)
}
