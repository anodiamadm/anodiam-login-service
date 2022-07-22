package com.anodiam.StudentSignup;

import com.anodiam.StudentSignup.model.Permission;
import com.anodiam.StudentSignup.model.Role;
import com.anodiam.StudentSignup.model.User;
import com.anodiam.StudentSignup.model.common.MessageResponse;
import com.anodiam.StudentSignup.model.common.ResponseCode;
import com.anodiam.StudentSignup.serviceRepository.Permission.PermissionService;
import com.anodiam.StudentSignup.serviceRepository.Role.RoleService;
import com.anodiam.StudentSignup.serviceRepository.User.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JwtAuthApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private RoleService roleService;

	//	Role should not be fetched against invalid Role Name
	@Test
	public void testNegativeFindRoleByInvalidRoleName() throws Exception
	{
		String roleName="INVALID_ROLE";
		Role role = roleService.findByRoleName(roleName).get();
		assertEquals(ResponseCode.ROLE_NAME_INVALID.getMessage() + roleName,
				role.getMessageResponse().getMessage());
	}

	//	Correct Role should be fetched against valid Role Name
	@Test
	public void testPositiveFindRoleByValidRoleName() throws Exception
	{
		String roleName="USER";
		Role role = roleService.findByRoleName(roleName).get();
		assertEquals(ResponseCode.ROLE_NAME_EXISTS.getMessage() + role.getRoleName(),
				role.getMessageResponse().getMessage());
	}

	//	Permission should not be fetched against invalid Permission Name
	@Test
	public void testNegativeFindPermissionNameByInvalidPermissionName() throws Exception
	{
		String permissionName="INVALID_ACCESS";
		Permission permission = permissionService.findByPermissionName(permissionName).get();
		assertEquals(ResponseCode.PERMISSION_NAME_INVALID.getMessage() + permissionName,
				permission.getMessageResponse().getMessage());
	}

	//	Correct Permission should be fetched against valid Permission Name
	@Test
	public void testPositiveFindPermissionNameByValidPermissionName() throws Exception
	{
		String permissionName="STUDENT_ACCESS";
		Permission permission = permissionService.findByPermissionName(permissionName).get();
		assertEquals(ResponseCode.PERMISSION_NAME_EXISTS.getMessage()
						+ permission.getPermissionName(), permission.getMessageResponse().getMessage());
	}

	//	User should NOT be fetched against invalid Email
	@Test
	public void testNegativeFindUserByWrongEmail() throws Exception
	{
		String email="invalidemail@gmail.com";
		User foundUser = userService.findByUsername(email).get();
		assertEquals(ResponseCode.USER_NOT_REGISTERED.getMessage() + email,
				foundUser.getMessageResponse().getMessage());
	}

	//	Correct User should be fetched against valid Email
	@Test
	public void testPositiveFindUserByCorrectEmail() throws Exception
	{
		String email="pinaki.sen@gmail.com";
		User foundUser = userService.findByUsername(email).get();
		assertEquals(ResponseCode.USER_ALREADY_EXISTS.getMessage() + foundUser.getUsername(),
				foundUser.getMessageResponse().getMessage());
	}

	//	User should NOT be able to register by entering invalid email
	@Test
	public void testNegativeSaveUserInvalidEmail1() throws Exception
	{
		String email="invalidemail.noAt.gmail.com";
		String password="dhsa67$#LB1";
		User inputUser=new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.EMAIL_FORMAT_INVALID.getMessage() + inputUser.getUsername(),
				messageResponse.getMessage());
	}

	//	User should NOT be able to register by entering invalid email
	@Test
	public void testNegativeSaveUserInvalidEmail1_1() throws Exception
	{
		String email="invalidemail.twoAts@gma@il.com";
		String password="dhsa67$#LB1";
		User inputUser=new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.EMAIL_FORMAT_INVALID.getMessage() + inputUser.getUsername(),
				messageResponse.getMessage());
	}

	//	User should NOT be able to register by entering invalid email
	@Test
	public void testNegativeSaveUserInvalidEmail2() throws Exception
	{
		String email="invalidemail.noDotAfterAt@gmail";
		String password="dhsa67$#LB1";
		User inputUser=new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.EMAIL_FORMAT_INVALID.getMessage() + inputUser.getUsername(),
				messageResponse.getMessage());
	}

	//	User should NOT be able to register by entering invalid email
	@Test
	public void testNegativeSaveUserInvalidEmail3() throws Exception
	{
		String email="@gmail.com";
		String password="dhsa67$#LB1";
		User inputUser=new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.EMAIL_FORMAT_INVALID.getMessage() + inputUser.getUsername(),
				messageResponse.getMessage());
	}

	//	User should NOT be able to register by entering invalid email
	@Test
	public void testNegativeSaveUserInvalidEmail4() throws Exception
	{
		String email="invalidemail.noCharacterBetweenAndDot@.com";
		String password="dhsa67$#LB1";
		User inputUser=new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.EMAIL_FORMAT_INVALID.getMessage() + inputUser.getUsername(),
				messageResponse.getMessage());
	}

	//	User should NOT be able to register by entering invalid email
	@Test
	public void testNegativeSaveUserInvalidEmail5() throws Exception
	{
		String email="invalidemail.noAlphabetAfterLastDot@gmail.99";
		String password="dhsaj67$#LB1";
		User inputUser=new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.EMAIL_FORMAT_INVALID.getMessage() + inputUser.getUsername(),
				messageResponse.getMessage());
	}

	//	User should NOT be able to register by entering existing / duplicate email
	@Test
	public void testNegativeSaveUserDuplicateEmail() throws Exception
	{
		String email="pinaki.sen@gmail.com";
		String password="ABcd@12";
		User inputUser=new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.USER_ALREADY_EXISTS.getMessage() + inputUser.getUsername(),
				messageResponse.getMessage());
	}

	//	If password < 6 characters, user should NOT be able to register
	@Test
	public void testNegativeSaveUserShortPassword() throws Exception
	{
		String email="abcd.smallpassword@gmail.com";
		String password="a@12A";
		User inputUser=new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.PASSWORD_SHORT.getMessage() + inputUser.getPassword(),
				messageResponse.getMessage());
	}

	//	If password > 100 charsacters, user should NOT be able to register
	@Test
	public void testNegativeSaveUserLongPassword() throws Exception
	{
		String email="abcd.bigpassword@gmail.com";
		String password="0123456qwertyABCD@#$0123456qwertyABCD@#$0123456qwertyABCD@#$0123456qwertyABCD@#$0123456qwertyABCD@#$x";
		User inputUser=new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.PASSWORD_INVALID.getMessage() + inputUser.getPassword(),
				messageResponse.getMessage());
	}

	//	If password does not contain characters [a-z], user should NOT be able to register
	@Test
	public void testNegativeSaveUserPasswordNotContainsSmallChar() throws Exception {
		String email = "password.without.a-z@gmail.com";
		String password = "ABCD@$123";
		User inputUser = new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.PASSWORD_INVALID.getMessage() + inputUser.getPassword(),
				messageResponse.getMessage());
	}

	//	If password does not contain characters [A-Z], user should NOT be able to register
	@Test
	public void testNegativeSaveUserPasswordNotContainsCapsChar() throws Exception
	{
		String email = "password.without.A-Z@gmail.com";
		String password = "abcd@$123";
		User inputUser = new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.PASSWORD_INVALID.getMessage() + inputUser.getPassword(),
				messageResponse.getMessage());
	}

	//	If password does not contain numerals [0-9], user should NOT be able to register
	@Test
	public void testNegativeSaveUserPasswordNotContainsNumericChar() throws Exception
	{
		String email = "password.without.0-9@gmail.com";
		String password = "abcdABCD@$";
		User inputUser = new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.PASSWORD_INVALID.getMessage() + inputUser.getPassword(),
				messageResponse.getMessage());
	}

	//	If password does not contain special chars, user should NOT be able to register
	@Test
	public void testNegativeSaveUserPasswordNotContainsSpecialChar() throws Exception
	{
		String email = "password.withoutSplChar@gmail.com";
		String password = "abcdABCD12";
		User inputUser = new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.PASSWORD_INVALID.getMessage() + inputUser.getPassword(),
				messageResponse.getMessage());
	}

	//	User should be able to register by entering valid email and password
	@Test
	public void testPositiveSuccessfulRegistration() throws Exception
	{
		String randomNumber=GenerateRandomNumber();
		String preFixEmail="pinaki".concat(randomNumber.substring(1,6));
		String email=preFixEmail.concat(".sen@gmail.com");
		String password="Pinaki@12";
		User inputUser=new User(email, password);
		MessageResponse messageResponse = userService.save(inputUser);
		assertEquals(ResponseCode.SUCCESS.getMessage() + inputUser.getUsername(),
				messageResponse.getMessage());
	}

//	*******************************************************************************
//					      Utility functions for unit test cases
//	*******************************************************************************

//	1. Random number generation
	private String GenerateRandomNumber()
	{
		try
		{
			return String.valueOf(ThreadLocalRandom.current().nextInt());
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			return "";
		}
	}
}
