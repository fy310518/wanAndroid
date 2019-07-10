package com.gcstorage.parkinggather;

public interface UAVHttpAddress {
    //互联网测试环境
    String HOST = "120.79.194.253";
    String PORT = "8768";

    //DMZ环境
//    String HOST = "113.57.174.98";
//    String PORT = "8768";

    String HOST_ADDRESS = "http://" + HOST + ":" + PORT;

    //上传图片
    String UPLOADIMAGE_URL = HOST_ADDRESS + "/Falcon/2.0/tools/uploadfile";

    //无人机任务列表
    String UAV_TASK_LIST = HOST_ADDRESS + "/UAV/task/serchTaskList";

    //无人机管理,任务搜索接口
    String UAV_TASK_SEARCH = HOST_ADDRESS + "/UAV/task/serchTaskList";

    //创建无人机群组任务
    String UAV_TASK_CREATE_GROUP = HOST_ADDRESS + "/UAV/task/createTask";

    //根据任务群组ID,获取任务的详情
    String UAV_TASK_INFO = HOST_ADDRESS + "/UAV/task/serchTaskDetail";

    //根据任务群组ID,线上传递的嫌疑人信息,
    String UAV_TASK_SUSPECT = HOST_ADDRESS + "/UAV/task/serchTaskSign";

    //获取任务中指令列表
    String UAV_TASK_INSTRUCTION_LIST = HOST_ADDRESS + "/UAV/instruction/serchInstructionList";

    //根据指令列表的数据查询指令详情
    String UAV_TASK_INSTRUCTION_INFO = HOST_ADDRESS + "/UAV/instruction/serchInstructionDetail";

    // 根据指令ID,获取人员和无人机的参数
    String UAV_POINTINFO = HOST_ADDRESS + "/UAV/uva/getPointInfo";

    //根据群组中的任务ID,获取人员和无人机的参数
    String UAV_TASK_POINTINFO = HOST_ADDRESS + "/UAV/uva/getPointInfo";

    //更新任务状态
    String UAV_TASK_STATE = HOST_ADDRESS + "/UAV/instruction/receiveInstruction";

    //任务回告接口
    String UAV_TASK_RETURNNOTICE = HOST_ADDRESS + "/UAV/instruction/finishInstruction";

    //预案列表
    String UAV_PLAN_LIST = HOST_ADDRESS + "/UAV/plan/serchPlanList";

    //创建指令:上传指令标记点
    String UAV_TAKS_UPDATA_INSTRUCTION = HOST_ADDRESS + "/UAV/instruction/createInstruction";

    //查询用户是否备勤
    String UAV_TAKS_UPDATA_STATUS = HOST_ADDRESS + "/UAV/work/serchUserStatus";

    //备勤状态更新
    String UAV_TAKS_SPARE_UPDATA = HOST_ADDRESS + "/UAV/work/updateWorkType";

    //获取自己创建的标记点
    String UAV_TAKS_SIGNBY_LOCAL = HOST_ADDRESS + "/UAV/task/serchTaskSignByLocal";

    //群组创建标记点
    String UAV_TAKS_CREATE_MAKER = HOST_ADDRESS + "/UAV/task/addTaskSign";

    //获取无人机的视频流
    String UAV_TAKS_VIDEO= HOST_ADDRESS + "/UAV/instruction/getFlyLive";

}
