package com.fulfilltrack.FulfillTrack.features.pedido;

import com.fulfilltrack.FulfillTrack.common.exception.ArchivoInvalidoException;
import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoImportErrorDTO;
import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoImportResultDTO;
import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.pedido.dto.PedidoResponseDTO;
import com.fulfilltrack.FulfillTrack.features.pedido.item.dto.ItemPedidoRequestDTO;
import com.fulfilltrack.FulfillTrack.features.producto.IProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoCsvService implements IPedidoCsvService {

    private final IPedidoService pedidoService;
    private final IProductoService productoService;

    @Override
    public PedidoImportResultDTO importarDesdeCsv(MultipartFile archivo) {
        Map<String, PedidoRequestDTO> pedidosPorOrden = new LinkedHashMap<>();
        List<PedidoResponseDTO> creados = new ArrayList<>();
        List<PedidoImportErrorDTO> errores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {

            String linea;
            boolean primeraLinea = true;
            int nroLinea = 0;

            while ((linea = reader.readLine()) != null) {
                nroLinea++;
                if (primeraLinea) { primeraLinea = false; continue; }
                if (linea.isBlank()) continue;

                String[] campos = linea.split(",");
                if (campos.length < 6) {
                    errores.add(PedidoImportErrorDTO.builder()
                            .numeroOrden("línea " + nroLinea)
                            .motivo("formato inválido: se esperaban 6 columnas")
                            .build());
                    continue;
                }

                try {
                    String numeroOrden        = campos[0].trim();
                    String direccionEntrega   = campos[1].trim();
                    String nombreDestinatario = campos[2].trim();
                    UUID empresaUuid          = UUID.fromString(campos[3].trim());
                    String sku                = campos[4].trim();
                    int cantidad              = Integer.parseInt(campos[5].trim());

                    UUID productoUuid = productoService.obtenerProductoPorSku(sku).getUuid();

                    PedidoRequestDTO pedido = pedidosPorOrden.computeIfAbsent(numeroOrden, k ->
                            new PedidoRequestDTO(numeroOrden, direccionEntrega, nombreDestinatario, empresaUuid, new ArrayList<>()));

                    pedido.getItems().add(new ItemPedidoRequestDTO(cantidad, productoUuid));
                } catch (Exception e) {
                    errores.add(PedidoImportErrorDTO.builder()
                            .numeroOrden("línea " + nroLinea)
                            .motivo("error al procesar la línea: " + e.getMessage())
                            .build());
                }
            }

        } catch (Exception e) {
            throw new ArchivoInvalidoException("No se pudo leer el archivo CSV: " + e.getMessage());
        }

        for (PedidoRequestDTO pedidoRequest : pedidosPorOrden.values()) {
            try {
                creados.add(pedidoService.crearPedido(pedidoRequest));
            } catch (Exception e) {
                errores.add(PedidoImportErrorDTO.builder()
                        .numeroOrden(pedidoRequest.getNumeroOrden())
                        .motivo(e.getMessage())
                        .build());
            }
        }

        return PedidoImportResultDTO.builder()
                .pedidosCreados(creados.size())
                .pedidosConError(errores.size())
                .creados(creados)
                .errores(errores)
                .build();
    }
}