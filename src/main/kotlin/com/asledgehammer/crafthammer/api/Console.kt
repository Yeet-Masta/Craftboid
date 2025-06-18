package com.asledgehammer.crafthammer.api

import com.asledgehammer.crafthammer.api.command.CommandSender
import com.asledgehammer.crafthammer.api.command.Messagable
import com.asledgehammer.crafthammer.api.permission.PermissionSupported

/**
 * **Console** TODO: Document.
 *
 * @author Jab
 */
interface Console : Messagable, PermissionSupported, CommandSender
