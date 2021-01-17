package com.zhouxiaoge.dag.tasks.impl.algorithm;

import com.zhouxiaoge.dag.exceptions.CyclicGraphDetectedException;
import com.zhouxiaoge.dag.models.Task;
import com.zhouxiaoge.dag.models.TaskTopo;
import com.zhouxiaoge.dag.models.impl.DefaultTaskTopo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 周小哥
 */
public class DFSTopoSorting {

    private HashSet<String> tmpMarks;

    private HashSet<String> permMarks;

    private LinkedList<Task> results;

    private Map<String, Task> taskMap;

    private Map<String, Integer> groupMap;

    private Task[] tasks;

    public DFSTopoSorting(Task[] tasks) {
        this.tasks = tasks;
        this.taskMap = Arrays.stream(tasks)
                .collect(Collectors.toMap(Task::getId, Function.identity()));
    }

    public DFSTopoSorting sort() {
        this.permMarks = new HashSet<String>();
        this.tmpMarks = new HashSet<String>();
        this.results = new LinkedList<Task>();

        for (Task task : tasks) {
            visit(task);
        }

        assignGroup();

        return this;
    }

    void visit(Task task) {
        String id = task.getId();

        if (permMarks.contains(id)) {
            return;
        }
        if (tmpMarks.contains(id)) {
            throw new CyclicGraphDetectedException(id);
        }
        tmpMarks.add(id);

        for (String adj : task.getDependants()) {
            visit(taskMap.get(adj));
        }

        tmpMarks.remove(id);
        permMarks.add(id);

        results.addFirst(task);
    }

    private void assignGroup() {
        this.groupMap = new HashMap<>(16);
        for (Task task : tasks) {
            groupMap.put(task.getId(), 0);
        }

        for (Task task : results) {
            Integer parentLevel = groupMap.get(task.getId());
            for (String adj : task.getDependants()) {
                groupMap.compute(adj, (k, v) -> Math.max(v, parentLevel + 1));
            }
        }
    }

    public TaskTopo[] topo() {
        Map<String, List<String>> invertedEdges = new HashMap<>(16);
        for (Task task : tasks) {
            for (String adj : task.getDependants()) {
                invertedEdges.computeIfAbsent(adj, k -> new ArrayList<>())
                        .add(task.getId());
            }
        }
        return results.stream()
                .map(task -> toTaskTopo(invertedEdges, task))
                .toArray(TaskTopo[]::new);
    }

    private DefaultTaskTopo toTaskTopo(Map<String, List<String>> invertedEdges, Task task) {
        List<String> depended = invertedEdges.getOrDefault(task.getId(), new ArrayList<>());
        return new DefaultTaskTopo(task, groupMap.get(task.getId()), depended.toArray(new String[0]));
    }

    public Task[] results() {
        return this.results();
    }
}
