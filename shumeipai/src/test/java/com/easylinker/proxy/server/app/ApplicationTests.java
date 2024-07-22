package com.easylinker.proxy.server.app;


import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
//    @Autowired
//    AppUserService appUserService;
//    @Autowired
//    UserRoleService userRoleService;
//    @Autowired
//    DeviceGroupService deviceGroupService;
//
//    @Autowired
//    DrivingRecordService drivingRecordService;
//    @Test
//    public void addDefaultAccount() throws Exception {
//
//        if (appUserService.getAUserById(1L) == null) {
//            AppUser appUser = new AppUser();
//            appUser.setId(1L);
//            appUser.setUsername("administrator");
//            appUser.setPassword(MD5Generator.EncodingMD5("administrator"));
//            appUser.setEmail("administrator@admin.com");
//            appUser.setPhone("8888888888");
//            appUserService.save(appUser);
//            //普通用户的角色
//            UserRole userRole = new UserRole();
//            userRole.setAppUser(appUser);
//            userRole.setRole("ROLE_USER");
//            userRoleService.save(userRole);
//
//            // 默认用户是管理员
//
//            UserRole adminRole = new UserRole();
//            adminRole.setAppUser(appUser);
//            adminRole.setRole("ROLE_ADMIN");
//            userRoleService.save(adminRole);
//
//            System.out.println("默认用户创建成功.");
//        }else {
//            System.out.println("默认用户已存在.");
//        }
//        System.out.println("初始化工作完成.");
//
//    }
//
//    @Test
//    public void testDriving() throws Exception{
//
//        JSONArray jsonArray=drivingRecordService.getGpsRecordByHash("xxxx");
//        System.out.println(jsonArray.toJSONString());
//    }
//
//    @Test
//    public void testGroupRecord() throws Exception{
//        JSONArray a=deviceGroupService.getAllDeviceGroupeRecordDataByPage(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createTime")));
//        System.out.println(a.toJSONString());
//    }
}
