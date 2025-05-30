declare module 'redux-persist/integration/react' {
  import { ReactNode } from 'react'
  import { Persistor } from 'redux-persist'
  interface PersistGateProps {
    loading?: ReactNode
    persistor: Persistor
    children?: ReactNode
  }

  export class PersistGate extends React.Component<PersistGateProps> {}
}
