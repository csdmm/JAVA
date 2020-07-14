package zsp_2701170229.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * 中文过滤器
 */
public class EncodingFilter implements Filter{

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig)  {
		
	}

}
