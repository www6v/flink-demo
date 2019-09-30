//package com.flink.common.mybatis;
//
//import org.apache.ibatis.session.SqlSession;
//
//class RtcBillDataAccess {
//
////    public static void insertDB(String appId, String roomId, Integer count,String profile,
////                                Long endTime, Long startTime,
////                                Integer videoCount, Integer audioCount,
////                                Date insertTime) {
//
//        public static void insertDB(value: (String,  String, Long, Long, Long, Boolean, Integer,Integer,Integer)) {
//
//
//        RoomStatus rtcBillEntity = new RoomStatus();
////        rtcBillEntity.setAppId(appId);
////        rtcBillEntity.setRoomId(roomId);
////        rtcBillEntity.setProfile(profile);
////        rtcBillEntity.setCount(count);
////        rtcBillEntity.setEndTime(endTime);
////        rtcBillEntity.setStartTime(startTime);
////        rtcBillEntity.setVideoCount(videoCount);
////        rtcBillEntity.setAudioCount(audioCount);
////        rtcBillEntity.setInsertTime(insertTime);
//
//        SqlSession sqlSession = MybatisUtil.getSqlSession();
//        RoomStatusMapper rtcBillMapper = sqlSession.getMapper(RoomStatusMapper.class);
//
//        int i = rtcBillMapper.insertRoomStatus(rtcBillEntity);
//
//        sqlSession.commit();
//        sqlSession.close();
//    }
//}