import { PayloadAction, createSlice } from '@reduxjs/toolkit'

import { CreateGroupRequest, Group, JoinGroupsRequest } from '@/types/group'

interface GroupState {
  inviteCode: string
  create: CreateGroupRequest
  group: Group
  join: JoinGroupsRequest
}

const initialState: GroupState = {
  inviteCode: '',
  create: {
    groupName: '',
    maxParticipants: 1,
    ownerNickname: '',
    ownerProfileImage: '',
  },
  group: {
    id: 0,
    name: '',
    inviteCode: '',
    leaderId: 0,
    maxParticipants: 0,
    members: [],
  },
  join: {
    groupId: 0,
    nickname: '',
    profileImage: '',
  },
}

const groupSlice = createSlice({
  name: 'group',
  initialState,
  reducers: {
    setInviteCode: (state, action: PayloadAction<string>) => {
      state.inviteCode = action.payload
    },
    setGroupName: (state, action: PayloadAction<string>) => {
      state.create.groupName = action.payload
    },
    setMaxParticipants: (state, action: PayloadAction<number>) => {
      state.create.maxParticipants = action.payload
    },
    setOwnerNickname: (state, action: PayloadAction<string>) => {
      state.create.ownerNickname = action.payload
    },
    setOwnerProfileImage: (state, action: PayloadAction<string>) => {
      state.create.ownerProfileImage = action.payload
    },
    setGroup: (state, action: PayloadAction<Group>) => {
      state.group = action.payload
    },
    setJoin: (state, action: PayloadAction<JoinGroupsRequest>) => {
      state.join = action.payload
    },
    setJoinGroupId: (state, action: PayloadAction<number>) => {
      state.join.groupId = action.payload
    },
    setJoinNickname: (state, action: PayloadAction<string>) => {
      state.join.nickname = action.payload
    },
    setJoinProfileImage: (state, action: PayloadAction<string>) => {
      state.join.profileImage = action.payload
    },
    clearJoin: (state) => {
      Object.assign(state.join, initialState.join)
    },
    clearCreate: (state) => {
      Object.assign(state.create, initialState.create)
    },
    clearGroup: (state) => {
      Object.assign(state.group, initialState.group)
    },
  },
})

export const {
  setInviteCode,
  setGroupName,
  setMaxParticipants,
  setOwnerNickname,
  setOwnerProfileImage,
  setGroup,
  setJoin,
  setJoinGroupId,
  setJoinNickname,
  setJoinProfileImage,
  clearJoin,
  clearCreate,
  clearGroup,
} = groupSlice.actions
export default groupSlice.reducer
