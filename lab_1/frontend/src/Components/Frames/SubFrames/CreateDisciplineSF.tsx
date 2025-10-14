import { useEffect, useState } from "react"
import ParameterField from "../FrameComponents/ParameterField"
import { processIsNotNull, processIsPositive, processIsValidInt, processStringMaxLength } from "../../../Utils/validations"
import ErrorPanel from "../FrameComponents/ErrorPanel"
import { makeQueryCreate } from "../../../Utils/baseMaps"
import { createObject } from "../../../Utils/apiQueries"

function CreateDisciplineSF({ onClose }: { onClose: () => void }) {
    const [name, setName] = useState("")
    const [labs_count, setlabsCount] = useState("")
    const [errorMsgs, setErrorMsg] = useState<string[]>([])

    useEffect(() => {
        let res : string[] = []
        processStringMaxLength(name, 400, "Имя", res)
        processIsNotNull(name, "Имя", res)
        processIsValidInt(labs_count, false, "Кол-во лаб", res)
        processIsPositive(labs_count, false, "Кол-во лаб", res)
        setErrorMsg(res)
    }, [name, labs_count]);

    function createQuery(){
        let form = new URLSearchParams()
        form.append('name', name)
        form.append('labs_count', labs_count)
        createObject(makeQueryCreate("Discipline"),form)
        onClose()
    }

    return (
        <>
            <div className="modal-main-content">
              <div className="modal-header">Создание Coordinate</div>
                <div className="modal-params-container">
                    <ParameterField value={name} setValue={setName} required={true} type="text" field="Имя" />
                    <ParameterField value={labs_count} setValue={setlabsCount} required={true} type="intNum" field="Кол-во лаб" />
                </div>
                <ErrorPanel errorMessages={errorMsgs} />  
            </div>
            <div className="modal-buttons">
                <button className="modal-close-button" onClick={onClose}>Закрыть</button>
                <button className={`${errorMsgs.length != 0 && "modal-button-disbled"} modal-save-button`} onClick={createQuery}>
                    Создать
                </button>
            </div>
        </>
    )
}

export default CreateDisciplineSF