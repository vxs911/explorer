package edu.georgetown.explorer.security

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
class SecurityInfoController extends grails.plugins.springsecurity.ui.SecurityInfoController {
}