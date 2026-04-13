// 프론트엔드 내부에서 사용할 정제된 인터페이스
export interface Skill {
    id: number;
    name: string;
}

export interface FreelancerProfile {
    id: number;
    nickname: string;
    profileImageUrl?: string;
    introduction: string;
    location: string;
    hourlyRate: number;
    workStyle: 'ONLINE' | 'OFFLINE' | 'HYBRID';
    skills: Skill[];
    averageRating: number;
}

// 백엔드 API 응답(DTO) 형태에 맞춘 인터페이스
export interface ApiSkill {
    skillId: number;
    name: string;
}

export interface ApiFreelancerDto {
    profileId: number;
    userName: string;
    profileImageUrl?: string;
    introduction: string;
    location: string;
    hourlyRate: number;
    workStyle: 'ONLINE' | 'OFFLINE' | 'HYBRID';
    skills: ApiSkill[];
    averageRating: number | null;
}

// API DTO를 프론트엔드 인터페이스로 안전하게 변환해주는 매퍼 함수
export function mapFreelancerDtoToProfile(dto: ApiFreelancerDto): FreelancerProfile {
    return {
        id: dto.profileId,
        nickname: dto.userName,
        profileImageUrl: dto.profileImageUrl,
        introduction: dto.introduction,
        location: dto.location,
        hourlyRate: dto.hourlyRate,
        workStyle: dto.workStyle,
        skills: dto.skills ? dto.skills.map(skill => ({
            id: skill.skillId,
            name: skill.name,
        })) : [],
        // averageRating이 null일 경우 0으로 안전하게 처리
        averageRating: dto.averageRating ?? 0,
    };
}
