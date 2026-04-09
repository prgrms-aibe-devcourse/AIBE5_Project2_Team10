import axios, { AxiosInstance, InternalAxiosRequestConfig } from "axios";

// 1. Axios 인스턴스 생성 (타입 지정으로 any 탈출)
const api: AxiosInstance = axios.create({
    baseURL: "http://localhost:8080",
    // 필요한 경우 timeout 등을 추가할 수 있음
});

// 2. 요청(Request) 인터셉터 설정
api.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        // 브라우저 환경에서만 localStorage에 접근 (Next.js SSR 방어)
        if (typeof window !== "undefined") {
            const token = localStorage.getItem("accessToken");
            if (token) {
                // 규격에 맞게 Authorization 헤더 추가
                config.headers.Authorization = `Bearer ${token}`;
            }
        }
        return config;
    },
    (error: unknown) => {
        // 에러 타입을 unknown 또는 Error로 지정하여 TS7006 방지
        return Promise.reject(error);
    }
);

export default api;