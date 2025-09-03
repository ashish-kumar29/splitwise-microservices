package com.ashish.splitwise.GroupService.Service;

import com.ashish.splitwise.GroupService.Dao.GroupDao;
import com.ashish.splitwise.GroupService.Model.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GroupServiceImplTest {

    @Mock
    private GroupDao groupDao;

    @InjectMocks
    private GroupServiceImpl groupService;

    private Group group;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        group = Group.builder()
                .id(1L)
                .groupName("Goa vacation")
                .description("Expense spent on goa vacation")
                .build();
    }


    @Test
    void testCreateGroup(){
        when(groupDao.save(any(Group.class))).thenReturn(group);
        Group newGroup = groupService.createGroup(group);
        assertEquals("Goa vacation", newGroup.getGroupName());
        verify(groupDao, times(1)).save(group);

    }

    @Test
    void testGetGroupById(){
        when(groupDao.findById(1L)).thenReturn(Optional.of(group));

        Group found = groupService.getGroupById(1l);


        assertEquals("Goa vacation", found.getGroupName());
        verify(groupDao, times(1)).findById(1l);
    }


}
