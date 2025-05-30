declare module 'react-mobile-picker' {
  export interface PickerProps<T extends Record<string, string>> {
    value: T
    onChange: (value: T, key: string) => void
    wheelMode?: 'natural' | 'normal'
    children?: React.ReactNode
  }

  interface PickerColumnProps {
    name: string
    children: React.ReactNode
  }

  interface PickerItemProps {
    value: string
    children: React.ReactNode
  }

  interface PickerComponent {
    <T extends Record<string, string>>(props: PickerProps<T>): JSX.Element
    Column: React.FC<PickerColumnProps>
    Item: React.FC<PickerItemProps>
  }

  const Picker: PickerComponent

  export default Picker
}
