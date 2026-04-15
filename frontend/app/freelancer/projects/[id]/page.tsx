'use client';

import React, { useEffect, useState } from 'react';
import api from '@/app/lib/axios';
import { useParams, useRouter } from 'next/navigation';
import {
    Calendar,
    MapPin,
    DollarSign,
    Globe,
    ArrowLeft,
    Clock,
    Briefcase,
    ChevronRight,
    Sparkles // 프리미엄 느낌을 위한 아이콘 추가
} from 'lucide-react';

interface ProjectDetail {
    projectId: number;
    companyName: string;
    projectName: string;
    budget: number;
    deadline: string;
    detail: string;
    status: string;
    online: boolean;
    offline: boolean;
    location: string;
    latitude: number;
    longitude: number;
    skills: string[];
}

export default function ProjectDetailPage() {
    const params = useParams();
    const router = useRouter();
    const id = params?.id as string;

    const [project, setProject] = useState<ProjectDetail | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchProjectDetail = async () => {
            if (!id) return;
            try {
                const response = await api.get(`/v1/projects/${id}`);
                setProject(response.data);
            } catch (err: any) {
                setError("프로젝트 정보를 불러오는데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };
        fetchProjectDetail();
    }, [id]);

    const formatBudget = (amount: number) => {
        return new Intl.NumberFormat('ko-KR', {
            style: 'currency',
            currency: 'KRW',
            maximumFractionDigits: 0
        }).format(amount);
    };

    if (loading) return (
        <div className="min-h-screen flex items-center justify-center bg-[#F9F8FF]">
            <div className="animate-pulse flex flex-col items-center">
                <div className="w-12 h-12 border-4 border-violet-500 border-t-transparent rounded-full animate-spin"></div>
                <p className="mt-4 text-violet-600 font-medium">멋진 프로젝트를 불러오는 중...</p>
            </div>
        </div>
    );

    if (error || !project) return (
        <div className="min-h-screen flex items-center justify-center bg-[#F9F8FF]">
            <div className="text-center p-8 bg-white rounded-2xl shadow-xl shadow-violet-100">
                <p className="text-red-500 text-lg font-bold mb-4">{error || "프로젝트를 찾을 수 없습니다."}</p>
                <button onClick={() => router.back()} className="text-violet-600 hover:text-violet-800 font-bold inline-flex items-center">
                    <ArrowLeft className="w-4 h-4 mr-2" /> 목록으로 돌아가기
                </button>
            </div>
        </div>
    );

    return (
        <div className="min-h-screen bg-[#F9F8FF] pb-20">
            {/* 상단 네비게이션 */}
            <nav className="max-w-5xl mx-auto p-4 flex items-center">
                <button
                    onClick={() => router.back()}
                    className="p-2 hover:bg-violet-100 rounded-full transition-all group"
                >
                    <ArrowLeft className="w-6 h-6 text-gray-600 group-hover:text-violet-600" />
                </button>
            </nav>

            <main className="max-w-4xl mx-auto px-6">
                {/* 1. 헤더 영역 */}
                <section className="mb-10 text-center md:text-left">
                    <div className="flex flex-wrap items-center justify-center md:justify-start gap-3 mb-4">
                        <span className="bg-violet-600 text-white px-4 py-1.5 rounded-full text-xs font-bold shadow-md shadow-violet-200">
                            {project.status === 'OPEN' ? '모집 중' : project.status}
                        </span>
                        <div className="flex items-center text-violet-500 text-sm font-semibold">
                            <Sparkles className="w-4 h-4 mr-1" />
                            Premium Project
                        </div>
                    </div>
                    <h1 className="text-4xl md:text-5xl font-black text-slate-900 leading-tight mb-6">
                        {project.projectName}
                    </h1>
                    <div className="flex items-center justify-center md:justify-start text-slate-500 font-medium">
                        <Briefcase className="w-4 h-4 mr-2 text-violet-400" />
                        <span className="mr-4">{project.companyName}</span>
                        <MapPin className="w-4 h-4 mr-2 text-violet-400" />
                        <span>{project.location}</span>
                    </div>
                </section>

                {/* 2. 핵심 정보 카드 그리드 */}
                <section className="grid grid-cols-1 md:grid-cols-2 gap-5 mb-12">
                    <div className="bg-white p-6 rounded-3xl shadow-sm hover:shadow-md transition-shadow border border-violet-50 flex items-center gap-5">
                        <div className="p-4 bg-violet-50 rounded-2xl">
                            <DollarSign className="w-7 h-7 text-violet-600" />
                        </div>
                        <div>
                            <p className="text-slate-400 text-xs font-bold uppercase tracking-wider mb-1">예상 금액</p>
                            <p className="text-2xl font-black text-slate-900">{formatBudget(project.budget)}</p>
                        </div>
                    </div>

                    <div className="bg-white p-6 rounded-3xl shadow-sm hover:shadow-md transition-shadow border border-violet-50 flex items-center gap-5">
                        <div className="p-4 bg-fuchsia-50 rounded-2xl">
                            <Calendar className="w-7 h-7 text-fuchsia-600" />
                        </div>
                        <div>
                            <p className="text-slate-400 text-xs font-bold uppercase tracking-wider mb-1">모집 마감</p>
                            <p className="text-2xl font-black text-slate-900">{project.deadline}</p>
                        </div>
                    </div>

                    <div className="bg-white p-6 rounded-3xl shadow-sm hover:shadow-md transition-shadow border border-violet-50 flex items-center gap-5">
                        <div className="p-4 bg-indigo-50 rounded-2xl">
                            <Globe className="w-7 h-7 text-indigo-600" />
                        </div>
                        <div>
                            <p className="text-slate-400 text-xs font-bold uppercase tracking-wider mb-1">근무 방식</p>
                            <p className="text-xl font-extrabold text-slate-900">
                                {project.online && project.offline ? "온/오프라인 병행" : project.online ? "원격(온라인)" : "상주(오프라인)"}
                            </p>
                        </div>
                    </div>

                    <div className="bg-white p-6 rounded-3xl shadow-sm hover:shadow-md transition-shadow border border-violet-50 flex items-center gap-5">
                        <div className="p-4 bg-purple-50 rounded-2xl">
                            <Clock className="w-7 h-7 text-purple-600" />
                        </div>
                        <div>
                            <p className="text-slate-400 text-xs font-bold uppercase tracking-wider mb-1">활동 지역</p>
                            <p className="text-xl font-extrabold text-slate-900">
                                {project.location ? project.location.split(' ')[1] || "지역 미정" : "위치 정보 없음"}
                            </p>
                        </div>
                    </div>
                </section>

                {/* 3. 기술 스택 섹션 */}
                <section className="mb-12">
                    <h3 className="text-2xl font-black text-slate-900 mb-6 flex items-center">
                        요구 스택
                        <div className="ml-3 h-1 flex-1 bg-violet-100 rounded-full"></div>
                    </h3>
                    <div className="flex flex-wrap gap-3">
                        {project.skills?.map((skill, index) => (
                            <span
                                key={index}
                                className="bg-white text-violet-700 border border-violet-100 px-5 py-2.5 rounded-2xl text-sm font-bold shadow-sm hover:bg-violet-600 hover:text-white transition-all cursor-default"
                            >
                                {skill}
                            </span>
                        ))}
                    </div>
                </section>

                {/* 4. 프로젝트 상세 내용 */}
                <section className="mb-12">
                    <h3 className="text-2xl font-black text-slate-900 mb-6">상세 업무 가이드</h3>
                    <div className="bg-white border border-violet-50 p-10 rounded-[2rem] leading-relaxed text-slate-700 whitespace-pre-wrap shadow-sm text-lg italic font-medium">
                        "{project.detail}"
                    </div>
                </section>

                {/* 5. 하단 액션 바 */}
                <div className="sticky bottom-8 flex justify-center w-full px-4">
                    <button className="w-full max-w-xl bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-700 hover:to-indigo-700 text-white font-black py-6 rounded-2xl transition-all shadow-2xl shadow-violet-200 hover:scale-[1.03] active:scale-[0.97] flex items-center justify-center gap-3 group">
                        이 프로젝트에 지금 지원하기
                        <ChevronRight className="w-6 h-6 group-hover:translate-x-2 transition-transform" />
                    </button>
                </div>
            </main>
        </div>
    );
}