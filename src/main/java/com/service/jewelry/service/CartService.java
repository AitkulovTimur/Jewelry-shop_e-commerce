package com.service.jewelry.service;

import com.service.jewelry.model.CartEntity;
import com.service.jewelry.model.ItemEntity;
import com.service.jewelry.model.ProductEntity;
import com.service.jewelry.repo.CartRepository;
import com.service.jewelry.repo.ItemRepository;
import com.service.jewelry.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ItemRepository itemRepository;

    public CartEntity createCart(int userId) {
        return cartRepository.save(CartEntity.builder().userId(userId).build());
    }

    public CartEntity getCartByUId(int userId) {

        List<CartEntity> cartEntities = cartRepository.findAllByUserId(userId);

        if (cartEntities.size() > 1) {
            cartRepository.deleteById(cartEntities.get(0).getId());
        }

        if (!cartRepository.existsByUserId(userId))
            return createCart(userId);

        return cartRepository.findByUserId(userId);
    }

    @Transactional
    public void addNewItemToCart(int vendorCode, int userId) {
        ProductEntity product = productRepository.findById(vendorCode).orElseThrow(() -> new RuntimeException("Not found"));

        if (product.getQuantity() == 0)
            throw new RuntimeException("Извините, товара нет в наличии");

        CartEntity cartEntity = cartRepository.findByUserId(userId);

        if (cartEntity == null)
            cartEntity = createCart(userId);

        List<ItemEntity> itemsInCart = cartEntity.getItems();

        if (!CollectionUtils.isEmpty(itemsInCart) && itemsInCart.stream().anyMatch(item -> item.getProductEntity().getVendorCode() == vendorCode)) {
            itemsInCart = itemsInCart.stream()
                    .map(item -> {
                        if (item.getProductEntity().getVendorCode() == vendorCode) {
                            int quantity = item.getQuantity() + 1;
                            if (quantity > product.getQuantity())
                                throw new RuntimeException("Товар закончился");
                            return item.withQuantity(quantity);
                        }
                        return item;
                    }).toList();

            cartRepository.saveAndFlush(cartEntity.withItems(itemsInCart));
        } else {
            ItemEntity itemEntity = ItemEntity.builder().quantity(1).productEntity(product).cart(cartEntity).build();
            itemRepository.save(itemEntity);

            itemsInCart.add(itemEntity);

            cartRepository.saveAndFlush(cartEntity.withItems(itemsInCart));
        }
    }

//    public void removeItemFromCart(int vendorCode, int userId) {
//        ProductEntity product = productRepository.findById(vendorCode).orElseThrow(() -> new RuntimeException("Not found"));
//
//        CartEntity cartEntity = cartRepository.findByUserId(userId);
//        if(cartEntity.getItems() == null)
//            cartEntity.setItems(new ArrayList<>());
//
//        List<ProductEntity> productEntityList = cartEntity.getItems();
//
//        productEntityList.remove(product);
//
//        cartRepository.save(cartEntity);
//    }

}
