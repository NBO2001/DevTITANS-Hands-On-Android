package com.example.plaintext.data.repository

import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.model.Password
import com.example.plaintext.data.model.PasswordInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Interface que define as operações de acesso ao banco de dados para gerenciamento de senhas.
 */
interface PasswordDBStore {

    /**
     * Obtém uma lista de todas as senhas armazenadas no banco de dados.
     *
     * @return Um [Flow] contendo uma lista de objetos [Password].
     */
    fun getList(): Flow<List<Password>>

    /**
     * Adiciona uma nova senha ao banco de dados.
     *
     * @param password O objeto [Password] a ser inserido.
     * @return O ID da senha inserida, representado como um [Long].
     */
    suspend fun add(password: Password): Long

    /**
     * Atualiza uma senha existente no banco de dados.
     *
     * @param password O objeto [Password] com os dados atualizados.
     */
    suspend fun update(password: Password)

    /**
     * Recupera uma senha do banco de dados pelo seu ID.
     *
     * @param id O ID da senha a ser recuperada.
     * @return O objeto [Password] correspondente ao ID, ou null se não encontrado.
     */
    fun get(id: Int): Password?

    /**
     * Salva uma senha no banco de dados, convertendo um objeto [PasswordInfo] em [Password].
     *
     * @param passwordInfo O objeto [PasswordInfo] contendo os dados da senha a ser salva.
     */
    suspend fun save(passwordInfo: PasswordInfo)

    /**
     * Verifica se o banco de dados de senhas está vazio.
     *
     * @return Um [Flow] emitindo [true] se o banco de dados estiver vazio, [false] caso contrário.
     */
    suspend fun isEmpty(): Flow<Boolean>
}

class LocalPasswordDBStore(
    private val passwordDao: PasswordDao
) : PasswordDBStore {
    override fun getList(): Flow<List<Password>> {
        return passwordDao.getAllPasswords()
    }

    override suspend fun add(password: Password): Long {
        return passwordDao.insert(password)
    }

    override suspend fun update(password: Password) {
        return passwordDao.update(password)
    }

    override fun get(id: Int): Password? {
        return passwordDao.get(id)
    }

    override suspend fun save(passwordInfo: PasswordInfo) {
        passwordDao.insert(passwordInfo.toPassword())
    }

    override suspend fun isEmpty(): Flow<Boolean> {
        return passwordDao.getAllPasswords().map { it.isEmpty() }
    }
}