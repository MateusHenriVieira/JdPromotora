package com.synko.api.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.synko.api.model.Tarifa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class TarifaRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "tarifas";

    /**
     * Salva ou atualiza uma tarifa.
     * Se o ID for nulo, cria um novo documento e seta a data de cadastro.
     */
    public String save(Tarifa tarifa) throws ExecutionException, InterruptedException {
        if (tarifa.getId() == null) {
            tarifa.setId(firestore.collection(COLLECTION_NAME).document().getId());
            tarifa.setDataCadastro(Timestamp.now());
        }

        firestore.collection(COLLECTION_NAME).document(tarifa.getId()).set(tarifa).get();
        return tarifa.getId();
    }

    /**
     * Busca uma tarifa específica pelo seu ID.
     */
    public Tarifa findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return document.toObject(Tarifa.class);
        }
        return null;
    }

    /**
     * Lista todas as tarifas de um banco específico.
     */
    public List<Tarifa> findAllByBancoId(String bancoId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("bancoId", bancoId)
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Tarifa> tarifas = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            tarifas.add(document.toObject(Tarifa.class));
        }
        return tarifas;
    }

    /**
     * Deleta uma tarifa pelo seu ID.
     */
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    /**
     * Lista todas as tarifas (método auxiliar, caso necessário).
     */
    public List<Tarifa> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Tarifa> tarifas = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            tarifas.add(document.toObject(Tarifa.class));
        }
        return tarifas;
    }
}