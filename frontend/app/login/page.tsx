"use client";

export default function LoginPage() {
    // [보고] 백엔드 구글 인증 엔드포인트로 직접 이동시킴
    const handleGoogleLogin = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/google";
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="max-w-md w-full p-10 bg-white shadow-2xl rounded-3xl">
                <h1 className="text-3xl font-extrabold mb-8 text-center text-gray-800">Dev Near</h1>
                <p className="text-center text-gray-500 mb-10">재능과 지역을 잇는 기지에 오신 것을 환영합니다.</p>

                <div className="space-y-4">
                    {/* 구글 로그인 버튼 */}
                    <button
                        onClick={handleGoogleLogin}
                        className="w-full flex items-center justify-center gap-3 border-2 border-gray-200 p-4 rounded-xl hover:bg-gray-50 transition-all font-semibold text-gray-700"
                    >
                        <img
                            src="https://www.gstatic.com/images/branding/product/1x/gsa_512dp.png"
                            alt="Google"
                            className="w-6 h-6"
                        />
                        Google 계정으로 시작하기
                    </button>
                </div>

                <div className="mt-8 pt-6 border-t border-gray-100 text-center">
                    <p className="text-sm text-gray-400">일반 회원가입은 아직 공사 중입니다 🚧</p>
                </div>
            </div>
        </div>
    );
}