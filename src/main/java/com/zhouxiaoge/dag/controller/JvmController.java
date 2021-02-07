package com.zhouxiaoge.dag.controller;

import com.zhouxiaoge.dag.cache.DagCacheUtils;
import com.zhouxiaoge.dag.models.BatchExecution;
import com.zhouxiaoge.dag.tasks.impl.storages.MemBasedTaskStorage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 周小哥
 * @date 2021年02月07日 22点42分
 */
@RestController
public class JvmController {

    /**
     * 获取操作系统信息
     *
     * @return 操作系统信息
     */
    @GetMapping("/systemInfo")
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        OperatingSystemMXBean osb = ManagementFactory.getOperatingSystemMXBean();
        systemInfo.put("Name", osb.getName());
        systemInfo.put("Arch", osb.getArch());
        systemInfo.put("AvailableProcessors", osb.getAvailableProcessors());
        systemInfo.put("Version", osb.getVersion());
        systemInfo.put("SystemLoadAverage", osb.getSystemLoadAverage());
        return systemInfo;
    }

    /**
     * 获取JVM的内存
     *
     * @return JVM的内存
     */
    @GetMapping("/jvm/info")
    public Map<String, Map<String, Object>> getJvmInfo() {
        Map<String, Map<String, Object>> jvmMap = new HashMap<>();
        MemoryMXBean mxb = ManagementFactory.getMemoryMXBean();
        Map<String, Object> heapMap = new HashMap<>();
        heapMap.put("Max", mxb.getHeapMemoryUsage().getMax() / 1024 / 1024 + "MB");    //Max:1776MB
        heapMap.put("Init", mxb.getHeapMemoryUsage().getInit() / 1024 / 1024 + "MB");  //Init:126MB
        heapMap.put("Committed", mxb.getHeapMemoryUsage().getCommitted() / 1024 / 1024 + "MB");   //Committed:121MB
        heapMap.put("Used", mxb.getHeapMemoryUsage().getUsed() / 1024 / 1024 + "MB");  //Used:7MB
        heapMap.put("init", mxb.getHeapMemoryUsage().toString());    //init = 132120576(129024K) used = 8076528(7887K) committed = 126877696(123904K) max = 1862270976(1818624K)

        Map<String, Object> nonHeapMap = new HashMap<>();
        nonHeapMap.put("Max", mxb.getNonHeapMemoryUsage().getMax() / 1024 / 1024 + "MB");    //Max:0MB
        nonHeapMap.put("Init", mxb.getNonHeapMemoryUsage().getInit() / 1024 / 1024 + "MB");  //Init:2MB
        nonHeapMap.put("Committed", mxb.getNonHeapMemoryUsage().getCommitted() / 1024 / 1024 + "MB");   //Committed:8MB
        nonHeapMap.put("Used", mxb.getNonHeapMemoryUsage().getUsed() / 1024 / 1024 + "MB");  //Used:7MB
        nonHeapMap.put("init", mxb.getNonHeapMemoryUsage().toString());    //init = 2555904(2496K) used = 7802056(7619K) committed = 9109504(8896K) max = -1(-1K)

        jvmMap.put("Heap", heapMap);
        jvmMap.put("NonHeap", nonHeapMap);
        return jvmMap;
    }

    /**
     * 获取JVM的内存池情况
     *
     * @return JVM的内存池情况
     */
    @GetMapping("/jvm/memory")
    public List<Map<String, Object>> getJvmMemory() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<MemoryPoolMXBean> mxb = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean memoryPoolMXBean : mxb) {
            Map<String, Object> mxbMap = new HashMap<>();
            mxbMap.put("Name:", memoryPoolMXBean.getName());
            mxbMap.put("Usage:", memoryPoolMXBean.getUsage());
            mxbMap.put("Manager:", String.join(",", memoryPoolMXBean.getMemoryManagerNames()));
            mxbMap.put("Type:", memoryPoolMXBean.getType());
            list.add(mxbMap);
        }
        return list;
    }

    /**
     * 获取程序线程信息
     *
     * @return 程序线程信息
     */
    @GetMapping("/jvm/pool")
    public Map<String, List<Map<String, Object>>> getJvmPool() {
        List<Map<String, Object>> activePoolList = new ArrayList<>();
        ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
        for (long id : tmx.getAllThreadIds()) {
            ThreadInfo ti = tmx.getThreadInfo(id);
            Map<String, Object> activeMap = new HashMap<>();
            activeMap.put("ThreadInfo-" + id, ti.toString().trim());
            activeMap.put("cpuTime:", tmx.getThreadCpuTime(id));
            activeMap.put("userTime:", tmx.getThreadUserTime(id));
            activePoolList.add(activeMap);
        }
        List<Map<String, Object>> deadPoolList = new ArrayList<>();
        if (tmx.findDeadlockedThreads() != null) {
            for (long id : tmx.findDeadlockedThreads()) {
                Map<String, Object> deadPoolMap = new HashMap<>();
                ThreadInfo ti = tmx.getThreadInfo(id);
                deadPoolMap.put("ThreadInfo-" + id, ti.toString().trim());
                deadPoolList.add(deadPoolMap);
            }
        }
        Map<String, List<Map<String, Object>>> map = new HashMap<>();
        map.put("ActivePool", activePoolList);
        map.put("DeadPool", deadPoolList);
        return map;
    }


}
