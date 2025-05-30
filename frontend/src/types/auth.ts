export interface SocialLogin {
  accessToken: string | null
  expiresIn: number | null
  refreshToken: string | null
}

export interface LoginRequest {
  emailAddress: string
  password: string
}

export interface SignUpRequest {
  emailAddress: string | null
  password: string | null
  name: string | null
}
