package com.kagan.chatapp.db.mappers

import com.kagan.chatapp.db.entities.UsersEntity
import com.kagan.chatapp.models.UserVM
import com.kagan.chatapp.utils.EntityMapper
import javax.inject.Inject

class NetworkMapper
@Inject
constructor() : EntityMapper<UsersEntity, UserVM> {

    override fun mapToEntity(dao: UserVM): UsersEntity {
        return UsersEntity(
            dao.Id.toString(),
            dao.UserName,
            dao.DisplayName
        )
    }

    fun mapListDaoToEntity(daoList: List<UserVM>): ArrayList<UsersEntity> {
        val entityList = arrayListOf<UsersEntity>()

        daoList.forEach {
            entityList.add(mapToEntity(it))
        }

        return entityList
    }
}