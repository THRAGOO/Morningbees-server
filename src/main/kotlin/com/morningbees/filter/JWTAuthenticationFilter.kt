package com.morningbees.filter

import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["/"], description = "인증 필터")
abstract class JWTAuthenticationFilter : Filter {
    override fun destroy() {
        super.destroy()
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        log.info("filter => API Token Filter");
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//        log.info("req header => {}", request.getHeader("x-auth-token"));
//        if ( request.getHeader("x-auth-token") == null ) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증오류");
//        }
//        chain.doFilter(req, res);
        var req:HttpServletRequest = request as HttpServletRequest
        var res:HttpServletResponse = response as HttpServletResponse
        if (req.getHeader("X-BEES-ACCESS-TOKEN") == "") {

        }
        chain?.doFilter(request, response);
    }

    public override fun init(filterConfig : FilterConfig) {
        throw ServletException("")
    }
}