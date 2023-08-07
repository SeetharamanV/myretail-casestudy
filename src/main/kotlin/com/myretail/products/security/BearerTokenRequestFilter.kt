package com.myretail.products.security

import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class BearerTokenRequestFilter(private val securityProperties: SecurityProperties) : GenericFilterBean() {
    companion object {
        private const val AUTH_HEADER = "authorization"
    }
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        val path = req.requestURI
        if (!path.contains("/v1")) {
            chain.doFilter(request, response)
            return
        }
        bearerTokenValidator(chain, request, response, req)
    }

    private fun bearerTokenValidator(chain: FilterChain, request: ServletRequest, response: ServletResponse, req: HttpServletRequest) {
        val authorization = req.getHeader(AUTH_HEADER)
        when {
            null == authorization -> {
                errorHandling(response, "Missing AUTHORIZATION TOKEN.", HttpServletResponse.SC_FORBIDDEN)
            }
            securityProperties.validAuthorizationToken == authorization -> {
                chain.doFilter(request, response)
            }
            else -> {
                errorHandling(response, "Invalid AUTHORIZATION TOKEN.", HttpServletResponse.SC_FORBIDDEN)
            }
        }
    }

    private fun errorHandling(response: ServletResponse, error: String, status: Int) {
        val resp = response as HttpServletResponse
        resp.reset()
        resp.status = status
        response.setContentLength(error.length)
        response.getWriter().write(error)
    }
}
