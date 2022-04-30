package com.wyj.controller;

import com.wyj.entity.Product;
import com.wyj.entity.Stockpile;
import com.wyj.model.security.Role;
import com.wyj.model.web.Result;
import com.wyj.service.ProductService;
import com.wyj.service.StockpileService;
import com.wyj.util.ResultUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/restful-api/products")
public class ProductController {
    private final ProductService productService;
    private final StockpileService stockpileService;

    public ProductController(ProductService productService, StockpileService stockpileService) {
        this.productService = productService;
        this.stockpileService = stockpileService;
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        return productService.getProduct(id);
    }

    /**
     * 根据产品查询库存
     */
    @GetMapping("/stockpile/{productId}")
    @RolesAllowed(Role.ADMIN)
    @PreAuthorize("#oauth2.hasAnyScope('BROWSER','SERVICE')")
    public Stockpile queryStockpile(@PathVariable("productId") Long productId) {
        return stockpileService.getStockpile(productId);
    }

    /**
     * 将指定的产品库存调整为指定数额
     */
    @PatchMapping("/stockpile/{productId}")
    @RolesAllowed(Role.ADMIN)
    @PreAuthorize("#oauth2.hasAnyScope('BROWSER')")
    public Result updateStockpile(@PathVariable("productId") Long productId, @RequestParam("amount") Integer amount) {
        stockpileService.setStockpileAmountByProductId(productId, amount);
        return ResultUtil.ok();
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/{id}")
    @RolesAllowed(Role.ADMIN)
    public Result removeProduct(@PathVariable("id") Long id) {
        productService.removeProduct(id);
        return ResultUtil.ok();
    }

    /**
     * 更新产品信息
     */
    @PutMapping
    @RolesAllowed(Role.ADMIN)
    public Result updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return ResultUtil.ok();
    }

    /**
     * 创建新的产品
     */
    @PostMapping
    @RolesAllowed(Role.ADMIN)
    public Product createProduct(@RequestBody Product product) {
        productService.createProduct(product);
        return product;
    }

}
