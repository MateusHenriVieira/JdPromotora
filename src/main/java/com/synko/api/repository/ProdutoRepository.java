package com.synko.api.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.synko.api.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class ProdutoRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "produtos";

    /**
     * Salva ou atualiza um produto.
     */
    public String save(Produto produto) throws ExecutionException, InterruptedException {
        if (produto.getId() == null) {
            produto.setId(firestore.collection(COLLECTION_NAME).document().getId());
            produto.setDataCadastro(Timestamp.now());
        }

        firestore.collection(COLLECTION_NAME).document(produto.getId()).set(produto).get();
        return produto.getId();
    }

    /**
     * Busca um produto específico pelo seu ID.
     */
    public Produto findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return document.toObject(Produto.class);
        }
        return null;
    }

    /**
     * Lista todos os produtos.
     */
    public List<Produto> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        return future.get().toObjects(Produto.class);
    }

    /**
     * Lista todos os produtos de um banco específico.
     */
    public List<Produto> findAllByBancoId(String bancoId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("bancoId", bancoId)
                .get();

        return future.get().toObjects(Produto.class);
    }

    /**
     * Lista todos os produtos de um tipo específico.
     */
    public List<Produto> findAllByTipo(String tipo) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("tipo", tipo)
                .get();

        return future.get().toObjects(Produto.class);
    }

    /**
     * Deleta um produto pelo seu ID.
     */
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }
}