import {
  Contract,
  ContractRequest,
  ContractUser,
  CreateContractResponse,
} from '@/types/contract'
import { CreateGroupRequest, Group, JoinGroupsRequest } from '@/types/group'

import { getRequest, postBooleanRequest, postRequest, putRequest } from './api'

//getContract
export const getContract = async (contractId: number) =>
  await getRequest<Contract>(`/contract/${contractId}`)

//updateContract
export const updateContract = async ({
  contractId,
  contract,
}: {
  contractId: number
  contract: ContractRequest
}) => await putRequest<Contract>(`/contract/${contractId}`, contract)

//confirmContract
export const confirmContract = async ({
  contractId,
  contract,
}: {
  contractId: number
  contract: ContractRequest
}) => await putRequest<Contract>(`/contract/${contractId}/pending`, contract)

//createGroup
export const createGroup = async (params: CreateGroupRequest) =>
  await postRequest<Group>('/groups', params)

//joinGroup
export const joinGroup = async (params: JoinGroupsRequest) =>
  await postRequest<Group>('/groups/join', params)

//createEmptyContract
export const createEmptyContract = async (params: { groupId: number }) =>
  await postRequest<CreateContractResponse>('/contract', params)

//approveContract
export const approveContract = async (
  contractId: number,
  params: { accountNo: string },
) => await postBooleanRequest(`/contract/${contractId}/approve`, params)

//getGroup
export const getGroup = async (groupId: number) =>
  await getRequest<Group>(`/groups/${groupId}`)

//getGroupByInviteCode
export const getGroupByInviteCode = async (inviteCode: string) =>
  await getRequest<Group>(
    `/groups/search?inviteCode=${encodeURIComponent(inviteCode)}`,
  )

//getContractMembers
export const getContractMembers = async (contractId: number) =>
  await getRequest<ContractUser[]>(`/contract/${contractId}/members`)

//createContractPDF
export const createContractPDF = async (contractId: number) =>
  await postRequest<{ presignedUrl: string }>(
    `/blockchain/contract/${contractId}/pdf`,
  )
