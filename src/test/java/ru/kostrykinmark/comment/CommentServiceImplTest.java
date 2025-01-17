package ru.kostrykinmark.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.kostrykinmark.comment.mapper.CommentMapper;
import ru.kostrykinmark.comment.repository.CommentRepository;
import ru.kostrykinmark.comment.service.CommentService;
import ru.kostrykinmark.role.repository.RoleRepository;
import ru.kostrykinmark.task.mapper.TaskMapper;
import ru.kostrykinmark.task.repository.TaskRepository;
import ru.kostrykinmark.user.mapper.UserMapper;
import ru.kostrykinmark.user.repository.UserRepository;

@ExtendWith(SpringExtension.class)
public class CommentServiceImplTest {


    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TaskRepository taskRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    @InjectMocks
    private CommentService commentService;


    @BeforeEach
    public void initialize() {

    }


}
