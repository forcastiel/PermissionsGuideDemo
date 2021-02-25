package com.auntec.permissionsguide

private val permissionGuideImpl:PermissionGuideImpl by lazy { PermissionGuideImpl() }

interface PermissionGuideAbility {
    val permissionGuideDao:PermissionGuideDao
        get() = permissionGuideImpl
}