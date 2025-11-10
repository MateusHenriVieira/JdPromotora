package com.synko.api.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.synko.api.model.Layout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class LayoutRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "layouts";

    /**
     * Salva ou atualiza um layout.
     * Se o ID for nulo, cria um novo documento e seta a data de cadastro.
     */
    public String save(Layout layout) throws ExecutionException, InterruptedException {
        if (layout.getId() == null) {
            layout.setId(firestore.collection(COLLECTION_NAME).document().getId());
            layout.setDataCadastro(Timestamp.now());
        }

        firestore.collection(COLLECTION_NAME).document(layout.getId()).set(layout).get();
        return layout.getId();
    }

    /**
     * Busca um layout específico pelo seu ID.
     */
    public Layout findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return document.toObject(Layout.class);
        }
        return null;
    }

    /**
     * Lista todos os layouts de um banco específico.
     */
    public List<Layout> findAllByBancoId(String bancoId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("bancoId", bancoId)
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Layout> layouts = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            layouts.add(document.toObject(Layout.class));
        }
        return layouts;
    }

    /**
     * Deleta um layout pelo seu ID.
     */
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    /**
     * Lista todos os layouts (método auxiliar, caso necessário).
     */
    public List<Layout> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Layout> layouts = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            layouts.add(document.toObject(Layout.class));
        }
        return layouts;
    }
}