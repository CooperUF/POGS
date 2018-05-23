package edu.mit.cci.pogs.view.taskgroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import edu.mit.cci.pogs.model.dao.task.TaskDao;
import edu.mit.cci.pogs.model.dao.taskgroup.TaskGroupDao;
import edu.mit.cci.pogs.model.jooq.tables.pojos.Task;
import edu.mit.cci.pogs.model.jooq.tables.pojos.TaskGroup;
import edu.mit.cci.pogs.model.jooq.tables.pojos.TaskGroupHasTask;
import edu.mit.cci.pogs.service.TaskGroupService;
import edu.mit.cci.pogs.utils.MessageUtils;
import edu.mit.cci.pogs.view.taskgroup.bean.TaskGroupBean;
import edu.mit.cci.pogs.view.taskgroup.bean.TaskGroupHasTaskBean;

@Controller
@RequestMapping(value = "/admin/taskgroups")
public class TaskGroupController {

    @Autowired
    private TaskGroupDao taskGroupDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private TaskGroupService taskGroupService;

    @ModelAttribute("tasks")
    public List<Task> getTasks() {
        //TODO: add the constraint for only the tasks associated with the current users research group
        return taskDao.list();
    }

    @GetMapping
    public String getTaskGroup(Model model) {

        model.addAttribute("taskGroupsList", taskGroupDao.list());
        return "taskgroup/taskgroup-list";
    }

    @GetMapping("{id}")
    public String getTaskGroups(@PathVariable("id") Long id, Model model) {

        model.addAttribute("taskGroup", taskGroupDao.get(id));
        return "taskgroup/taskgroup-display";
    }

    @GetMapping("/create")
    public String createTaskGroup(Model model) {

        model.addAttribute("taskGroup", new TaskGroup());
        return "taskgroup/taskgroup-edit";
    }

    @GetMapping("{id}/edit")
    public String createTaskGroup(@PathVariable("id") Long id, Model model) {
        TaskGroupBean bean = new TaskGroupBean(taskGroupDao.get(id));

        model.addAttribute("taskGroup", bean);
        List<TaskGroupHasTaskBean> currentTaskGroupHasTasks = resolveTaskGroupHasTaskBeans(
                taskGroupService.listTaskGroupHasTaskByTaskGroup(id));

        model.addAttribute("currentTaskGroupHasTasks", currentTaskGroupHasTasks);
        return "taskgroup/taskgroup-edit";
    }

    private List<TaskGroupHasTaskBean> resolveTaskGroupHasTaskBeans(List<TaskGroupHasTask> list) {
        List<TaskGroupHasTaskBean> ret = new ArrayList<>();
        for (TaskGroupHasTask tght : list) {
            TaskGroupHasTaskBean tghtb = new TaskGroupHasTaskBean();
            tghtb.setTaskId(tght.getTaskId());
            tghtb.setOrder(tght.getOrder() + 1);
            Task task = taskDao.get(tght.getTaskId());
            if (task != null) {
                tghtb.setTaskName(task.getTaskName());
                ret.add(tghtb);
            }
        }
        return ret;
    }

    @PostMapping
    public String saveTaskGroup(@ModelAttribute TaskGroupBean taskGroup, RedirectAttributes redirectAttributes) {

        taskGroupService.createOrUpdate(taskGroup);
        if (taskGroup.getId() == null) {

            MessageUtils.addSuccessMessage("TaskGroup created successfully!", redirectAttributes);
        } else {
            MessageUtils.addSuccessMessage("TaskGroup updated successfully!", redirectAttributes);
        }

        return "redirect:/admin/taskgroups";
    }

}
