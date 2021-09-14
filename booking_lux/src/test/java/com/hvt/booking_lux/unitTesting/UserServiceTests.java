package com.hvt.booking_lux.unitTesting;

import com.hvt.booking_lux.model.User;
import com.hvt.booking_lux.model.enumeration.Role;
import com.hvt.booking_lux.model.exceptions.PasswordNotMatchException;
import com.hvt.booking_lux.model.exceptions.UsernameAlreadyExistsException;
import com.hvt.booking_lux.repository.UserRepository;
import com.hvt.booking_lux.service.UserService;
import com.hvt.booking_lux.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        User user = new User("username","password","Name","LastName",Role.ROLE_ADMIN,"address","number");
        User user2 = new User("username2","password","Name","LastName",Role.ROLE_ADMIN,"address","number");
        Mockito.when(this.userRepository.save(user)).thenReturn(user);
        Mockito.when(this.userRepository.save(user2)).thenReturn(user2);
        Mockito.when(this.passwordEncoder.encode("password")).thenReturn("password");
        Mockito.when(this.passwordEncoder.encode("newPassword")).thenReturn("newPassword");
        Mockito.when(this.userRepository.findById("username2")).thenReturn(java.util.Optional.of(user2));
        List<User> list = new ArrayList<User>();
        list.add(user);
        list.add(user2);
        Mockito.when(this.userRepository.findAll()).thenReturn(list);

        this.userService = Mockito.spy(new UserServiceImpl(this.userRepository, this.passwordEncoder));
    }

    @Test
    public void testSuccessRegister() {
        User user = this.userService.register("username","password","password","Name","LastName");

        Assert.assertNotNull("User is null", user);
        Assert.assertNotEquals("Username is [blank]","",user.getUsername());
        Assert.assertNotEquals("Password is [blank]","",user.getPassword());
        Assert.assertEquals("Password doesn't match", "password", user.getPassword());
    }
    @Test
    public void testRegisterFailedUserNameExists(){
        Assert.assertThrows(UsernameAlreadyExistsException.class,()->this.userService.register("username2","pass","pass","Name","Last"));

    }
    @Test
    public void testPasswordNotMatch(){
        Assert.assertThrows(PasswordNotMatchException.class,()->this.userService.register("username","pass","pass1","name","l"));
    }

    @Test
    public void testEditUser() {
        User user = userService.edit("username2","FirstName","LastName","mail");
        Assert.assertEquals("FirstName doesnt match","FirstName",user.getFirstName());
        Assert.assertEquals("LastName doesnt match","LastName",user.getLastName());
    }

    @Test
    public void testPasswordChanger(){
        User user = userService.changePassword("username2","newPassword","newPassword");
        Assert.assertEquals("newPassword",user.getPassword());
    }

    @Test
    public void testPasswordChangerDifferentPasswords(){
        Assert.assertThrows(PasswordNotMatchException.class,()->userService.changePassword("username2","newPassword1","newPassword"));
    }

    @Test
    public void findAllUserNames(){
        List<String> list = this.userService.findAllUsernames();
        List<String> usernames = new ArrayList<String>();
        usernames.add("username");
        usernames.add("username2");
        Assert.assertEquals(list,usernames);
    }



}
