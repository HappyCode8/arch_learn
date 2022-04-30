package com.wyj.service;

import com.wyj.entity.Product;
import com.wyj.entity.Settlement;
import com.wyj.mapper.ProductMapper;
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

    /**
     * 提取产品信息而不是由客户端传上来避免用户修改值
     * */
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
