'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import FreelancerCard from '@/components/freelancer/FreelancerCard';
import { FreelancerProfile, ApiFreelancerDto, mapFreelancerDtoToProfile } from '@/types/freelancer';
import api from '../lib/axios';
import { Search, MapPin, SlidersHorizontal } from 'lucide-react';
import { motion } from 'framer-motion';

export default function ClientDashboard() {
    const router = useRouter();
    // [수정] any 배열 대신 강력하게 타입이 지정된 FreelancerProfile 배열 사용
    const [freelancers, setFreelancers] = useState<FreelancerProfile[]>([]);
    const [filter, setFilter] = useState({ skill: '', region: '', sort: 'id' });
    const [loading, setLoading] = useState(true);
    const [authorized, setAuthorized] = useState(false);

    // 🔥 커서 글로우
    const [cursor, setCursor] = useState({ x: 0, y: 0 });

    useEffect(() => {
        const move = (e: MouseEvent) => {
            setCursor({ x: e.clientX, y: e.clientY });
        };
        window.addEventListener("mousemove", move);
        return () => window.removeEventListener("mousemove", move);
    }, []);

    useEffect(() => {
        const checkAccess = async () => {
            const token = localStorage.getItem("accessToken");
            if (!token) {
                alert("로그인이 필요합니다.");
                router.replace("/login");
                return;
            }

            try {
                const res = await api.get("/v1/users/me");
                const role = res.data.role;

                if (role === "GUEST" || role === "ROLE_GUEST") {
                    router.replace("/onboarding");
                    return;
                }

                if (role === "FREELANCER" || role === "ROLE_FREELANCER") {
                    alert("해당 대시보드는 클라이언트 전용 화면입니다.");
                    router.replace("/");
                    return;
                }

                setAuthorized(true);
            } catch (err) {
                router.replace("/login");
            }
        };

        checkAccess();
    }, [router]);

    const fetchFreelancers = async () => {
        setLoading(true);
        try {
            const { data } = await api.get<ApiFreelancerDto[]>('/v1/freelancers', { params: filter });
            // [수정] 백엔드 응답(DTO)을 프론트엔드 전용 타입으로 매핑하여 상태에 저장
            const mappedData = data.map(mapFreelancerDtoToProfile);
            setFreelancers(mappedData);
        } catch (err) {
            console.error("인재를 불러오지 못했습니다!", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (authorized) {
            fetchFreelancers();
        }
    }, [filter, authorized]);

    const presetSkills = ['Java', 'React', 'Spring Boot', 'Figma', 'Node.js', 'Python', 'AWS'];

    if (!authorized) {
        return (
            <div className="flex min-h-screen items-center justify-center bg-zinc-50 text-[#FF7D00] font-black text-xl animate-pulse">
                AUTHORIZING ACCESS...
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-zinc-50 text-zinc-900 pb-20 relative overflow-hidden">

            {/* 🔥 커서 글로우 */}
            <div
                className="pointer-events-none fixed z-0 w-[300px] h-[300px] rounded-full bg-[#FF7D00]/20 blur-[120px] transition-all duration-200"
                style={{
                    left: cursor.x - 150,
                    top: cursor.y - 150
                }}
            />

            {/* NAV */}
            <nav className="w-full py-4 px-10 bg-white/80 backdrop-blur-xl border-b border-zinc-200 flex justify-between items-center sticky top-0 z-50">
                <div className="font-black text-2xl tracking-tight cursor-pointer" onClick={() => router.push("/")}>
                    <span className="text-[#FF7D00]">Dev</span>
                    <span className="text-zinc-900">Near</span>
                </div>

                <div className="flex gap-4 items-center">
                    <button
                        onClick={() => router.push('/profile')}
                        className="text-sm font-semibold text-zinc-500 hover:text-zinc-900 transition"
                    >
                        내 프로필
                    </button>

                    <button className="px-5 py-2 bg-[#FF7D00] text-white rounded-xl text-sm font-bold hover:brightness-110 transition shadow-md">
                        프로젝트 등록
                    </button>
                </div>
            </nav>

            {/* HEADER */}
            <section className="relative pt-24 pb-16 px-8 bg-white border-b border-zinc-200 overflow-hidden">

                {/* 🔥 인포그래픽 배경 */}
                <div className="absolute inset-0 pointer-events-none">
                    <svg className="absolute w-full h-full opacity-[0.06]" viewBox="0 0 1000 300">
                        <polyline
                            fill="none"
                            stroke="#FF7D00"
                            strokeWidth="2"
                            points="0,200 150,120 300,160 450,80 600,140 750,60 900,100 1000,70"
                        />
                    </svg>
                </div>

                <div className="max-w-6xl mx-auto relative z-10 text-center">

                    {/* 🔥 데이터 바 */}
                    <div className="flex justify-center gap-6 text-xs text-zinc-500 mb-4">
                        <div>🔥 매칭률 92%</div>
                        <div>📍 서울 프리랜서 1,284명</div>
                        <div>⚡ 평균 응답 3.2h</div>
                    </div>

                    <motion.h1
                        initial={{ y: 20, opacity: 0 }}
                        animate={{ y: 0, opacity: 1 }}
                        className="text-5xl font-black tracking-tight mb-4"
                    >
                        완벽한 파트너를 <br />
                        <span className="text-[#FF7D00]">데이터 기반</span>으로 찾으세요
                    </motion.h1>

                    <p className="text-zinc-500 mb-10">
                        DevNear는 개발자와 클라이언트를 가장 효율적으로 연결합니다.
                    </p>

                    {/* SEARCH */}
                    <div className="bg-white/90 backdrop-blur-md p-2 rounded-2xl shadow-xl border border-zinc-200 flex flex-col md:flex-row gap-2">

                        <div className="flex-1 flex items-center px-5 py-3 bg-zinc-50 rounded-xl border border-zinc-200 focus-within:border-[#FF7D00] transition">
                            <Search className="w-5 h-5 text-zinc-400 mr-2" />
                            <input
                                className="w-full bg-transparent outline-none text-sm"
                                placeholder="기술 스택 (React, Spring...)"
                                value={filter.skill}
                                onChange={(e) => setFilter({ ...filter, skill: e.target.value })}
                            />
                        </div>

                        <div className="flex-1 flex items-center px-5 py-3 bg-zinc-50 rounded-xl border border-zinc-200 focus-within:border-[#FF7D00] transition">
                            <MapPin className="w-5 h-5 text-zinc-400 mr-2" />
                            <input
                                className="w-full bg-transparent outline-none text-sm"
                                placeholder="지역 (서울, 부산...)"
                                value={filter.region}
                                onChange={(e) => setFilter({ ...filter, region: e.target.value })}
                            />
                        </div>

                        <button className="bg-[#FF7D00] text-white px-8 py-3 rounded-xl font-bold hover:brightness-110 transition">
                            탐색
                        </button>
                    </div>

                    {/* TAGS */}
                    <div className="mt-6 flex flex-wrap justify-center gap-2">
                        {presetSkills.map(s => (
                            <button
                                key={s}
                                onClick={() => setFilter({ ...filter, skill: filter.skill === s ? '' : s })}
                                className={`px-4 py-1.5 rounded-full text-sm font-semibold border transition ${
                                    filter.skill === s
                                        ? 'bg-[#FF7D00] text-white border-[#FF7D00]'
                                        : 'bg-white text-zinc-500 border-zinc-200 hover:border-[#FF7D00]'
                                }`}
                            >
                                {s}
                            </button>
                        ))}
                    </div>
                </div>
            </section>

            {/* LIST */}
            <main className="max-w-6xl mx-auto px-6 py-12">

                <div className="flex justify-between items-center mb-8">
                    <h2 className="text-lg font-bold">
                        결과 <span className="text-[#FF7D00]">{freelancers.length}</span>
                    </h2>

                    <div className="flex items-center gap-2 text-sm text-zinc-500">
                        <SlidersHorizontal className="w-4 h-4" />
                        <select
                            className="bg-transparent outline-none cursor-pointer"
                            value={filter.sort}
                            onChange={(e) => setFilter({ ...filter, sort: e.target.value })}
                        >
                            <option value="id">최신순</option>
                            <option value="rating">평점순</option>
                            <option value="projects">프로젝트순</option>
                        </select>
                    </div>
                </div>

                {loading ? (
                    <div className="flex justify-center py-24">
                        <div className="w-10 h-10 border-4 border-[#FF7D00] border-t-transparent rounded-full animate-spin"></div>
                    </div>
                ) : freelancers.length > 0 ? (

                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {freelancers.map((item) => (
                            // [수정] 매퍼 함수를 통해 완전하게 변환된 데이터(id 존재 보장)를 사용합니다.
                            <FreelancerCard key={item.id} data={item} />
                        ))}
                    </div>

                ) : (
                    <div className="text-center py-24 bg-white rounded-2xl border border-zinc-200 shadow-sm">
                        <h3 className="text-zinc-500 font-semibold">검색 결과 없음</h3>
                        <button
                            onClick={() => setFilter({ skill: '', region: '', sort: 'id' })}
                            className="mt-4 text-[#FF7D00] font-bold underline"
                        >
                            필터 초기화
                        </button>
                    </div>
                )}
            </main>
        </div>
    );
}
