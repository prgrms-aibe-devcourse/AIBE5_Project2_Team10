'use client';

import { motion } from 'framer-motion';
import { MapPin, Star } from 'lucide-react';
import { FreelancerProfile } from '@/types/freelancer';

interface Props {
    data: FreelancerProfile; // [수정] any가 아닌 강력한 타입 사용
}

export default function FreelancerCard({ data }: Props) {
    return (
        <motion.div
            whileHover={{ y: -6 }}
            className="group relative rounded-2xl border border-zinc-200 bg-white overflow-hidden transition-all duration-300 hover:shadow-xl hover:-translate-y-1"
        >

            {/* 상단 이미지 */}
            <div className="h-36 bg-gradient-to-br from-zinc-100 to-zinc-200">
                {data.profileImageUrl && (
                    <img
                        src={data.profileImageUrl}
                        alt={data.nickname}
                        className="w-full h-full object-cover group-hover:scale-105 transition duration-300"
                    />
                )}
            </div>

            <div className="p-4">

                {/* 이름 + 평점 */}
                <div className="flex justify-between items-center mb-2">
                    <h3 className="font-bold text-zinc-900 tracking-tight">{data.nickname}</h3>

                    <div className="flex items-center text-[#FF7D00] text-sm font-bold">
                        <Star size={14} fill="currentColor" />
                        <span className="ml-1">{data.averageRating.toFixed(1)}</span>
                    </div>
                </div>

                {/* 소개 */}
                <p className="text-xs text-zinc-500 line-clamp-2 mb-3">
                    {data.introduction}
                </p>

                {/* 스킬 */}
                <div className="flex flex-wrap gap-1 mb-3">
                    {data.skills.slice(0, 3).map((skill) => (
                        <span
                            // [수정] 매퍼에 의해 변환된 깨끗한 id를 그대로 사용합니다.
                            key={skill.id}
                            className="px-2 py-0.5 text-[10px] rounded-md bg-orange-50 text-[#FF7D00] border border-orange-100 font-semibold"
                        >
                            {skill.name}
                        </span>
                    ))}
                </div>

                {/* 하단 */}
                <div className="flex justify-between items-center pt-3 border-t border-zinc-100 text-xs text-zinc-500">
                    <div className="flex items-center">
                        <MapPin size={12} className="mr-1"/>
                        {data.location}
                    </div>

                    <div className="font-bold text-[#FF7D00]">
                        ₩{data.hourlyRate.toLocaleString()}
                    </div>
                </div>
            </div>
        </motion.div>
    );
}
