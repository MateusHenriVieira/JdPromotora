package com.synko.api.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.synko.api.model.MetaGerente; // Você precisará criar este Model
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class MetaGerenteRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "metasGerentes";

    /**
     * Salva ou atualiza uma meta de gerente.
     */
    public String save(MetaGerente meta) throws ExecutionException, InterruptedException {
        if (meta.getId() == null) {
            meta.setId(firestore.collection(COLLECTION_NAME).document().getId());
            meta.setDataCadastro(Timestamp.now());
        }

        firestore.collection(COLLECTION_NAME).document(meta.getId()).set(meta).get();
        return meta.getId();
    }

    /**
     * Busca uma meta específica pelo seu ID.
     */
    public MetaGerente findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return document.toObject(MetaGerente.class);
        }
        return null;
    }

    /**
     * Lista todas as metas de um gerente específico.
     */
    public List<MetaGerente> findAllByGerenteId(String gerenteId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("gerenteId", gerenteId)
                .get();

        return future.get().toObjects(MetaGerente.class);
    }

    /**
     * Deleta uma meta pelo seu ID.
     */
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }
}