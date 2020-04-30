import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PetComponentsPage, PetDeleteDialog, PetUpdatePage } from './pet.page-object';

const expect = chai.expect;

describe('Pet e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let petComponentsPage: PetComponentsPage;
  let petUpdatePage: PetUpdatePage;
  let petDeleteDialog: PetDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Pets', async () => {
    await navBarPage.goToEntity('pet');
    petComponentsPage = new PetComponentsPage();
    await browser.wait(ec.visibilityOf(petComponentsPage.title), 5000);
    expect(await petComponentsPage.getTitle()).to.eq('realEstateManagementServiceApp.pet.home.title');
    await browser.wait(ec.or(ec.visibilityOf(petComponentsPage.entities), ec.visibilityOf(petComponentsPage.noResult)), 1000);
  });

  it('should load create Pet page', async () => {
    await petComponentsPage.clickOnCreateButton();
    petUpdatePage = new PetUpdatePage();
    expect(await petUpdatePage.getPageTitle()).to.eq('realEstateManagementServiceApp.pet.home.createOrEditLabel');
    await petUpdatePage.cancel();
  });

  it('should create and save Pets', async () => {
    const nbButtonsBeforeCreate = await petComponentsPage.countDeleteButtons();

    await petComponentsPage.clickOnCreateButton();

    await promise.all([petUpdatePage.setNameInput('name'), petUpdatePage.setTypeInput('type'), petUpdatePage.setColorInput('color')]);

    expect(await petUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await petUpdatePage.getTypeInput()).to.eq('type', 'Expected Type value to be equals to type');
    expect(await petUpdatePage.getColorInput()).to.eq('color', 'Expected Color value to be equals to color');
    const selectedCertifiedAssistanceAnimal = petUpdatePage.getCertifiedAssistanceAnimalInput();
    if (await selectedCertifiedAssistanceAnimal.isSelected()) {
      await petUpdatePage.getCertifiedAssistanceAnimalInput().click();
      expect(await petUpdatePage.getCertifiedAssistanceAnimalInput().isSelected(), 'Expected certifiedAssistanceAnimal not to be selected')
        .to.be.false;
    } else {
      await petUpdatePage.getCertifiedAssistanceAnimalInput().click();
      expect(await petUpdatePage.getCertifiedAssistanceAnimalInput().isSelected(), 'Expected certifiedAssistanceAnimal to be selected').to
        .be.true;
    }

    await petUpdatePage.save();
    expect(await petUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await petComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Pet', async () => {
    const nbButtonsBeforeDelete = await petComponentsPage.countDeleteButtons();
    await petComponentsPage.clickOnLastDeleteButton();

    petDeleteDialog = new PetDeleteDialog();
    expect(await petDeleteDialog.getDialogTitle()).to.eq('realEstateManagementServiceApp.pet.delete.question');
    await petDeleteDialog.clickOnConfirmButton();

    expect(await petComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
