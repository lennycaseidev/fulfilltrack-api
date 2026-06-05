package com.fulfilltrack.FulfillTrack.features.pedido;

import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoImportResultDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IPedidoCsvService {
    PedidoImportResultDTO importarDesdeCsv(MultipartFile archivo);
}