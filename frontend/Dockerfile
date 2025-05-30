# ⚡ 1. React(Next.js) 빌드 단계
FROM node:18-alpine AS builder

WORKDIR /app

# package.json과 package-lock.json만 복사 후 의존성 설치 (최적화)
COPY package.json package-lock.json ./
RUN npm ci --production

# Next.js 소스 코드 복사 후 빌드 실행
COPY . .
RUN npm run build

# 🏃‍♂️ 2. 배포용 컨테이너 (Next.js 서버 실행)
FROM node:18-alpine

WORKDIR /app

# 빌드된 Next.js 파일과 필요한 패키지만 복사
COPY --from=builder /app/package.json ./
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/public ./public

EXPOSE 3000

# Next.js 프로덕션 실행
CMD ["npx", "next", "start"]