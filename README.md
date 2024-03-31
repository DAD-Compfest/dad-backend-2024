# DAD Backend with Springboot
[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/summary/new_code?id=Adpro-C4_backend)
# API and Docs
## Authentication Feature
1. ### ✅Verifikasi Email
Berguna untuk memverifikasi email sebelum register.

### Untuk Mengirim Kode Verifikasi ke email calon pengguna

`POST /auth/email/create-email-verification`
```json
{
  "email": "isadestroyed17@gmail.com",
  "subject": "Email Verification",
  "content": "Ini adalah token untuk pendaftaran akun DAD Compfest kamu:",
  "successMessage": "Kode verifikasi telah dikirimkan ke email Anda!"
}
```
`RESULT /auth/email/create-email-verification`
```json
{
    "data": {
        "message": "Kode verifikasi telah dikirimkan ke email Anda!"
    },
    "message": "Kode verifikasi telah dikirimkan ke email Anda!",
    "status": 202
}
```
### Untuk Memverifikasi Email Calon Pengguna
`POST /auth/email/verify`
```json
{
  "email": "isadestroyed17@gmail.com",
  "token": "557d79"
}
```

`RESULT /auth/email/verify`
```json
{
    "data": {
        "isVerified": true,
        "message": "Server telah memverifikasi token apakah sesuai atau tidak"
    },
    "message": "Server telah memverifikasi token apakah sesuai atau tidak",
    "status": 202
}
```

2. ### ✅Register
Untuk registrasi Admin maupun Team, perlu mengirimkan post request terhadap url ``/auth/register/<admin OR team>``
Pastikan sebelum registrasi, verifikasi email sudah dilakukan. Kalau tidak, registrasi tidak diterima.
#### Format yang benar untuk json request body:
- Sudah melalui verifikasi email

`POST /auth/register/admin`
```json
{
    "user": {
        "username": "admin123",
        "password": "adminPassword",
        "email":"isacitralearning@gmail.com"
    },
    "passwordConfirmation": "adminPassword"
}
```
`POST /auth/register/team`
```json
{
  "user": {
    "teamName": "Test Team",
    "teamUsername": "test_username",
    "teamEmail": "isadestroyed17@gmail",
    "password": "testpassword",
    "teamMembers": ["Anggota1", "Anggota2", "Anggota3"]
  },
  "passwordConfirmation": "testpassword"
}
```
`RESULT /auth/register/admin`
```json
{
    "data": {
        "message": "Berhasil mendaftarkan ADMIN"
    },
    "message": "Berhasil mendaftarkan ADMIN",
    "status": 202
}
```
`RESULT /auth/register/team`
```json
{
  "data": {
    "message": "Berhasil mendaftarkan tim test_username"
  },
  "message": "Berhasil mendaftarkan tim test_username",
  "status": 202
}
```
- Belum melalui verifikasi email

`POST /auth/register/team`
```json
{
  "user": {
    "teamName": "Test Team Ga Bener",
    "teamUsername": "teamgaverif",
    "teamEmail": "isacitralearning@gmail",
    "password": "testpassword",
    "teamMembers": ["Anggota1", "Anggota2", "Anggota3"]
  },
  "passwordConfirmation": "testpassword"
}
```
`RESULT /auth/register/team`
```json
{
  "data": {
    "message": "Email belum diverifikasi"
  },
  "message": "Email belum diverifikasi",
  "status": 401
}
```
#### Konfirmasi Password tidak sesuai:
`POST /auth/register/admin`
```json
{
    "user": {
        "username": "admin123",
        "password": "adminPassword",
        "email": "email@gmail.com"
    },
    "passwordConfirmation": "ucok"
}
```
`RESULT /auth/register/<admin OR team>`
```json
{
    "data": {
        "message": "Konfirmasi password tidak sesuai"
    },
    "message": "Konfirmasi password tidak sesuai",
    "status": 401
}
```
### ✅Login
Untuk login Admin maupun Team, perlu mengirimkan post request terhadap url ``/auth/login/<admin OR team>``

`POST /auth/login/admin`
```json
{
    "username": "admin123",
    "password": "adminPassword"
}
```
`POST /auth/login/team`
```json
{
  "username":"test_usernamexx",
  "password": "12345678"
}
```
`RESULT /auth/login/admin`
```json
{
  "data": {
    "admin": {
      "id": 1,
      "username": "admin123",
      "password": "$2a$10$R6uVnNFNwhV9KQvIq/h/DOHW.TDk/26vh31e2SROnR/ZJ6j.cJM8u",
      "email": "isacitralearning@gmail.com"
    },
    "adminToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEyMyIsImV4cCI6MTcxMTg4NjU1Mn0.sOjMMEy1iTnogQ2ubze67hUlMm3Oa0GMOnEzHSvgjH8"
  },
  "message": "Login sebagai Admin berhasil",
  "status": 202
}
```

`RESULT /auth/login/team`
```json
{
  "data": {
    "teamToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0X3VzZXJuYW1leHgiLCJleHAiOjE3MTE4OTk0Mzl9.3Fr3QFJP1gLio1IEIIWrZZeErmcKK5FjMRhVreZ2tyw",
    "team": {
      "teamId": 3,
      "teamName": "Test Team team",
      "teamUsername": "test_usernamexx",
      "teamEmail": "isaterganteng@gmail.com",
      "password": "$2a$10$UqbDyL9gspJBRlzOxTWyyePCT6OJjK8LvV8S92iwnyfqqo9GG3AoC",
      "teamMembers": "Anggota1,Anggota2,Anggota3",
      "teamMembersList": [
        "Anggota1",
        "Anggota2",
        "Anggota3"
      ]
    }
  },
  "message": "Login sebagai Tim berhasil",
  "status": 202
}
```
#### Contoh Invalid Login
Invalid login disebabkan User dengan username yang dimaksud tidak ditemukan. Bisa juga disebabkan username dan password tidak cocok.

`RESULT /auth/login/<admin OR customer>`
```json
{
    "data": {},
    "message": "Maaf username atau password tidak sesuai",
    "status": 401
}
```

### ✅Logout
Perlu mencantumkan header dengan key authorization dan token yang dimaksud.

`RESULT /auth/logout`
```json
{
    "data": {
        "message": "Berhasil logout",
        "status": "ACCEPTED"
    },
    "message": "Berhasil logout",
    "status": 202
}
```
Jika tidak ditemukan token yang dimaksud maka:

`RESULT /auth/logout`
```json
{
  "timestamp": "2024-03-29T08:57:36.987+00:00",
  "status": 400,
  "error": "Bad Request",
  "trace": "org.springframework.web.bind.MissingRequestHeaderException: Required request header 'Authorization' for method parameter type String is not present\r\n\tat org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver.handleMissingValue(RequestHeaderMethodArgumentResolver.java:87)\r\n\tat org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver.handleMissingValue(AbstractNamedValueMethodArgumentResolver.java:233)\r\n\tat org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver.resolveArgument(AbstractNamedValueMethodArgumentResolver.java:124)\r\n\tat org.springframework.web.method.support.HandlerMethodArgumentResolverComposite.resolveArgument(HandlerMethodArgumentResolverComposite.java:122)\r\n\tat org.springframework.web.method.support.InvocableHandlerMethod.getMethodArgumentValues(InvocableHandlerMethod.java:228)\r\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:182)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:920)\r\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:830)\r\n\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\r\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1089)\r\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979)\r\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)\r\n\tat org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:914)\r\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:590)\r\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885)\r\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:205)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)\r\n\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)\r\n\tat org.springframework.web.filter.CompositeFilter$VirtualFilterChain.doFilter(CompositeFilter.java:108)\r\n\tat org.springframework.security.web.FilterChainProxy.lambda$doFilterInternal$3(FilterChainProxy.java:231)\r\n\tat org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:365)\r\n\tat org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:126)\r\n\tat org.springframework.security.web.access.ExceptionTranslationFilter.doFilter(ExceptionTranslationFilter.java:120)\r\n\tat org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)\r\n\tat org.springframework.security.web.authentication.AnonymousAuthenticationFilter.doFilter(AnonymousAuthenticationFilter.java:100)\r\n\tat org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)\r\n\tat org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter.doFilter(SecurityContextHolderAwareRequestFilter.java:179)\r\n\tat org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)\r\n\tat org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63)\r\n\tat org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)\r\n\tat org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:107)\r\n\tat org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:93)\r\n\tat org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)\r\n\tat org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:91)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)\r\n\tat org.springframework.security.web.header.HeaderWriterFilter.doHeadersAfter(HeaderWriterFilter.java:90)\r\n\tat org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:75)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)\r\n\tat org.springframework.security.web.context.SecurityContextHolderFilter.doFilter(SecurityContextHolderFilter.java:82)\r\n\tat org.springframework.security.web.context.SecurityContextHolderFilter.doFilter(SecurityContextHolderFilter.java:69)\r\n\tat org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)\r\n\tat org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:62)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)\r\n\tat org.springframework.security.web.session.DisableEncodeUrlFilter.doFilterInternal(DisableEncodeUrlFilter.java:42)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.springframework.security.web.FilterChainProxy$VirtualFilterChain.doFilter(FilterChainProxy.java:374)\r\n\tat org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:233)\r\n\tat org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:191)\r\n\tat org.springframework.web.filter.CompositeFilter$VirtualFilterChain.doFilter(CompositeFilter.java:113)\r\n\tat org.springframework.web.servlet.handler.HandlerMappingIntrospector.lambda$createCacheFilter$3(HandlerMappingIntrospector.java:195)\r\n\tat org.springframework.web.filter.CompositeFilter$VirtualFilterChain.doFilter(CompositeFilter.java:113)\r\n\tat org.springframework.web.filter.CompositeFilter.doFilter(CompositeFilter.java:74)\r\n\tat org.springframework.security.config.annotation.web.configuration.WebMvcSecurityConfiguration$CompositeFilterChainProxy.doFilter(WebMvcSecurityConfiguration.java:230)\r\n\tat org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:352)\r\n\tat org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:268)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)\r\n\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)\r\n\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)\r\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\r\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174)\r\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149)\r\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)\r\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)\r\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482)\r\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115)\r\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)\r\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\r\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:344)\r\n\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:391)\r\n\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)\r\n\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:896)\r\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1744)\r\n\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)\r\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)\r\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\r\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)\r\n\tat java.base/java.lang.Thread.run(Thread.java:833)\r\n",
  "message": "Required header 'Authorization' is not present.",
  "path": "/auth/logout"
}
```

### Ganti Password
Ganti Password dapat dilakukan apabila verifikasi token email dijalankan sebelumnya (maksimal 10 menit).
`POST /auth/change-password/<admin OR team>`
```json
{
"user": {
"teamName": "Test Team team",
"teamUsername": "test_usernamexx",
"teamEmail": "isaterganteng@gmail.com",
"password": "12345678",
"teamMembers": ["Anggota1", "Anggota2", "Anggota3"]
},
"passwordConfirmation": "12345678"
}
```

