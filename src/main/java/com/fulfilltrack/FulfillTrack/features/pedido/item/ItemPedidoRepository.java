package com.fulfilltrack.FulfillTrack.features.pedido.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedidoEntity, Long> {
}
