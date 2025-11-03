import { useEffect, useState } from "react"
import ParameterField from "../frame-component/ParameterField"
import { processIsNotNull, processIsPositive, processIsValidInt, processStringMaxLength } from "../../../util/validations"
import ErrorPanel from "../frame-component/ErrorPanel"
import { makeQueryCreate, makeQueryDelete } from "../../../util/baseMaps"
import { createObject, removeObject } from "../../../util/apiQueries"
import type { Discipline } from "../../../interface/entity/Discipline"
import BaseFrame from "../BaseFrame"
import DeleteAlert from "./DeleteAlert"
import ShowSF from "./ShowSF"

function CreateDisciplineSF({ onClose, id, discipline }: { onClose: () => void, id: number, discipline: Discipline|undefined }) {
    const [name, setName] : [any, React.Dispatch<any>] = useState("")
    const [labs_count, setlabsCount] : [any, React.Dispatch<any>] = useState("")
    const [errorMsgs, setErrorMsg] = useState<string[]>([])

    const [alert, setAlert] = useState(false)
    const [modalOpen, setModalOpen] = useState("")
    function showConnected(){
        setModalOpen("ShowLabWork")
    }
    function modalOnClose(){
        setModalOpen("")
    }
    useEffect(() => {
        if(discipline != undefined){
            setName(discipline.name)
            setlabsCount(discipline.labsCount)
        }
    },[])

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
        if(id != -1) form.append('id', id.toString())
        form.append('name', name)
        form.append('labs_count', labs_count)
        createObject(makeQueryCreate("Discipline"),form)
        onClose()
    }

    function deleteQuery(){
        removeObject(makeQueryDelete("Discipline", id.toString()))
        .then(() =>onClose())
        .catch(() => {
            setAlert(true)
        })
    }

    return (
        <>
            <BaseFrame isOpen={alert} onClose={() => setAlert(false)} zindex={5000} width="30%" height="35%">
                <>
                    <DeleteAlert onClose={() => setAlert(false)}></DeleteAlert>
                </>
            </BaseFrame>
            <BaseFrame isOpen={modalOpen != ""} onClose={modalOnClose} zindex={1000} width="40%" height="">
                <>
                    {modalOpen=="ShowLabWork" && <ShowSF onClose={modalOnClose} objectName="LabWork" filterColumnEx="disciplineId" filterDataEx={id.toString()}/>}
                </>
            </BaseFrame>
            <div className="modal-main-content">
              <div className="modal-header">{id == -1 ? "Создание" : "Изменение"} Discipline</div>
                <div className="modal-params-container">
                    <ParameterField value={name} setValue={setName} required={true} type="text" field="Имя" />
                    <ParameterField value={labs_count} setValue={setlabsCount} required={true} type="intNum" field="Кол-во лаб" />
                </div>
                <ErrorPanel errorMessages={errorMsgs} />  
            </div>
            <div className="modal-buttons">
                <button className="modal-close-button" onClick={onClose}>Закрыть</button>
                {
                    discipline != undefined && 
                    <button className={`modal-save-button`} onClick={showConnected}>
                        Связанные
                    </button>
                }
                {
                    discipline != undefined && 
                    <button className={`modal-save-button`} onClick={deleteQuery}>
                        Удалить
                    </button>
                }
                <button className={`${errorMsgs.length != 0 && "modal-button-disbled"} modal-save-button`} onClick={createQuery}>
                    {id == -1 ? "Создать" : "Изменить"}
                </button>
            </div>
        </>
    )
}

export default CreateDisciplineSF