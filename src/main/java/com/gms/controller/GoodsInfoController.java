package com.gms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.gms.common.Result;
import com.gms.dto.GoodsInfoDTO;
import com.gms.entity.GoodsInfo;
import com.gms.entity.GoodsType;
import com.gms.service.GoodsInfoService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tszwaidai
 * @since 2024-06-07
 */
@RestController
@RequestMapping("/goodsInfo")
public class GoodsInfoController {
    @Autowired
    private GoodsInfoService goodsInfoService;

    /**
     * 分页查询所有物品信息 模糊查询与多表查询
     * @return
     */
    @GetMapping("/list")
    public Result getInfoList(@RequestParam(value = "goodsname", required = false) String goodsname,
                              @RequestParam(value = "typename", required = false) String typename,
                              @RequestParam(value = "pageNo") Long pageNo,
                              @RequestParam(value = "pageSize") Long pageSize) {
        Page<Map<String, Object>> page = new Page<>(pageNo, pageSize);
        // 创建联表查询包装器
        MPJLambdaWrapper<GoodsInfo> wrapper = new MPJLambdaWrapper<>();
        // 添加联表查询
        wrapper.selectAll(GoodsInfo.class)
                .select(GoodsType::getGoodsTypeName) // 选择类型名称
                .leftJoin(GoodsType.class, GoodsType::getGoodsTypeId, GoodsInfo::getGoodsTypeId);
        // 根据物品名称的搜索条件
        if (StringUtils.hasText(goodsname)) {
            wrapper.like(GoodsInfo::getGoodsName, goodsname);
        }
        // 添加根据分类名称的搜索条件
        if (StringUtils.hasText(typename)) {
            wrapper.like(GoodsType::getGoodsTypeName, typename);
        }

        IPage<Map<String, Object>> rawPage = goodsInfoService.pageMaps(page,wrapper);

        List<GoodsInfoDTO> goodsInfoDTOList = rawPage.getRecords().stream().map(record -> {
            GoodsInfoDTO dto = new GoodsInfoDTO();
            dto.setGoodsId((Integer) record.get("goodsId"));
            dto.setGoodsName((String) record.get("goodsName"));
            dto.setGoodsTypeId((Integer) record.get("goodsTypeId"));
            dto.setGoodsTypeName((String) record.get("goodsTypeName")); // 手动设置类型名称
            dto.setGoodsDesc((String) record.get("goodsDesc"));
            dto.setStatus((Integer) record.get("status"));
            dto.setGoodsPrice((BigDecimal) record.get("goodsPrice"));
            return dto;
        }).collect(Collectors.toList());

        // 创建DTO分页对象
        Page<GoodsInfoDTO> dtoPage = new Page<>(pageNo, pageSize, rawPage.getTotal());
        dtoPage.setRecords(goodsInfoDTOList);
        return Result.suc(dtoPage.getRecords(), dtoPage.getTotal());
    }


    /**
     * 新增物品信息
     * @param goodsInfo
     * @return
     */
    @PostMapping(value = "/add")
    public Result addInfo(@RequestBody GoodsInfo goodsInfo) {
        goodsInfoService.save(goodsInfo);
        return Result.suc("新增物品信息成功");
    }

    /**
     * 修改物品信息
     * @param goodsInfo
     * @return
     */
    @PutMapping(value = "/update")
    public Result updateInfo(@RequestBody GoodsInfo goodsInfo) {
        goodsInfoService.updateById(goodsInfo);
        return Result.suc("修改物品信息成功");
    }

    /**
     * 根据ID获得物品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getInfoById(@PathVariable("id") Integer id){
         GoodsInfo goodsInfo = goodsInfoService.getById(id);
         return Result.suc(goodsInfo);
    }

    /**
     * 删除物品信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") Integer id){
        goodsInfoService.removeById(id);
        return Result.suc("删除物品信息成功");
    }

    /**
     * 更新状态
     * @param goodsInfo
     * @return
     */
    @PostMapping("/updateStatus")
    public Result updateStatus(@RequestBody GoodsInfo goodsInfo) {
        boolean success = goodsInfoService.updateById(goodsInfo);
        if (success){
            return Result.suc("状态更新成功");
        }else{
            return Result.fail("状态更新失败");
        }
    }


}

