package com.synko.api.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.synko.api.model.MetaProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class MetaProdutoRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "metasProdutos";

    /**
     * Salva ou atualiza uma meta de produto.
     */
    public String save(MetaProduto meta) throws ExecutionException, InterruptedException {
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
    public MetaProduto findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return document.toObject(MetaProduto.class);
        }
        return null;
    }

    /**
     * Lista todas as metas de um produto específico.
     */
    public List<MetaProduto> findAllByProdutoId(String produtoId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("produtoId", produtoId)
                .get();

        return future.get().toObjects(MetaProduto.class);
    }

    /**
     * Deleta uma meta pelo seu ID.
     */
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }
}