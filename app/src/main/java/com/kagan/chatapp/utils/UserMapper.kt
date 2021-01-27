package com.kagan.chatapp.utils

interface EntityMapper<Entity, Dao> {

    fun mapToEntity(dao: Dao): Entity
}