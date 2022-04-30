package com.wyj.warehouse.service;

import com.wyj.entity.Product;
import com.wyj.entity.Settlement;
import com.wyj.warehouse.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    //todo: 提取出来这个map，同时重建数据库字段统一避免混乱
    public void replenishProductInformation(Settlement bill) {
        List<Long> ids = bill.getItems().stream().map((Settlement.Item::getProductId)).collect(Collectors.toList());
        bill.productMap = productMapper.selectAllByIdIn(ids).stream().collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    public List<Product> findAll() {
        return productMapper.selectAll();
    }

    public Product getProduct(Long id){
        return productMapper.selectOneById(id);
    }

    public void removeProduct(Long id) {
        productMapper.updateIsVisibleById(false, id);
    }

    public void updateProduct(Product product) {
        productMapper.updateSelectiveById(product);
    }

    public void createProduct(Product product){
        productMapper.insertSelective(product);
    }
}
