package com.management.clientinvoice.config;


import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * Returns the Spring managed bean instance of the given class type (if it exists).
     * Returns null otherwise.
     *
     * @param beanClass
     * @return
     */
    public static <T extends Object> T getBean(Class<T> beanClass) {
        if (beanClass == HttpServletRequest.class) {
            HttpServletRequest httpServletRequest =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                            .getRequest();
            return (T) httpServletRequest;
        }
        return context.getBean(beanClass);
    }

    public static <T extends Object> Object getBeanOfRepo(Class<?> beanClass) {
        return context.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {

        // store ApplicationContext reference to access required beans later on
        SpringContext.context = context;
    }
}

