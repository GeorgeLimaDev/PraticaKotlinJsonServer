package com.example.navegacao1.model.dados

import retrofit2.http.*

interface UsuarioServiceIF {

    @GET("usuarios")
    suspend fun listar(): List<Usuario>

    @GET("58013240/json/")
    suspend fun getEndereco(): Endereco

    @POST("usuarios")
    suspend fun inserir(@Body usuario: Usuario): Usuario

    @DELETE("usuarios/{id}")
    suspend fun remover(@Path("id") id: Int): Unit

    @GET("usuarios/{id}")
    suspend fun buscarPorId(@Path("id") id: Int): Usuario
}
