package com.myretail.products.security

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ApiKeyRequestFilter(private val securityProperties: SecurityProperties) : GenericFilterBean() {
    companion object {
        private const val API_KEY_PATH = "key"
        private const val API_KEY_HEADER = "x-api-key"
    }

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        val path = req.requestURI
        log.info("path:$path")
        if (!path.contains("/v1")) {
            chain.doFilter(request, response)
            return
        }
        apiKeyValidator(chain, request, response, req)
    }

    private fun apiKeyValidator(chain: FilterChain, request: ServletRequest, response: ServletResponse, req: HttpServletRequest) {
        val parameters = req.parameterMap
        val apiKeyFromPath = parameters[API_KEY_PATH]
        val apiKeyFromHeader = req.getHeader(API_KEY_HEADER)
        val key = if (apiKeyFromPath.isNullOrEmpty()) apiKeyFromHeader else apiKeyFromPath[0]
        when {
            null == key -> {
                errorHandling(response, "Missing API KEY.", HttpServletResponse.SC_UNAUTHORIZED)
            }
            securityProperties.validApiKeys.contains(key) -> {
                chain.doFilter(request, response)
            }
            else -> {
                errorHandling(response, "Invalid API KEY.", HttpServletResponse.SC_UNAUTHORIZED)
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
