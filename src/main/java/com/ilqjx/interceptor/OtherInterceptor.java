package com.ilqjx.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.User;
import com.ilqjx.service.CategoryService;
import com.ilqjx.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class OtherInterceptor implements HandlerInterceptor {

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int cartTotalItemNumber = 0;
        if (user != null) {
            List<OrderItem> orderItemList = orderItemService.listOrderItem(user, null);
            for (OrderItem oi : orderItemList) {
                cartTotalItemNumber += oi.getNumber();
            }
        }
        List<Category> categoryList = categoryService.listCategory();
        String contextPath = request.getContextPath();
        session.setAttribute("contextPath", contextPath);
        session.setAttribute("categories_below_search", categoryList);
        session.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
    }

}
